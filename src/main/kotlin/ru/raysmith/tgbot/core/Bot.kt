package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import ru.raysmith.tgbot.core.handler.BaseEventHandlerFactory
import ru.raysmith.tgbot.core.handler.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.base.CommandHandler
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.response.NetworkResponse
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramApiException
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.asParameter
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsDSL
import ru.raysmith.tgbot.utils.locations.LocationsWrapper
import ru.raysmith.utils.letIf
import ru.raysmith.utils.properties.PropertiesFactory
import java.io.IOException
import java.util.*
import kotlin.system.measureTimeMillis


// TODO impl string-length safe util (sendMessage -> text.take(4096)), throw error or loop sending
/**
 * Creates bot instance
 *
 * @param token Bot token from [@BotFather](https://t.me/botfather).
 * Can be null if is set via environment variable or properties
 * @param timeout Timeout in seconds for long polling. Defaults to 50.
 * Should be positive, short polling should be used for testing purposes only.
 * @param scope Coroutine scope for new updates
 * @param lastUpdateId Identifier of the first update to be returned. Must be greater by one than the highest among
 * the identifiers of previously received updates. If null, updates starting with the earliest unconfirmed update
 * are returned. An update is considered confirmed as soon as getUpdates is called with an offset higher than
 * its update_id. The negative offset can be specified to retrieve updates starting from -offset update from the end
 * of the updates queue. All previous updates will be forgotten.
 * */
class Bot(
    val token: String? = null,
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    private var lastUpdateId: Int? = null, // TODO can be negative, reverse mode?
) : ApiCaller {

    init {
        check(timeout >= 0) { "timeout should be positive" }
    }

    private var stoppingJob: Job? = null

    // callbacks
    private var onError: (e: Exception) -> Unit = { }
    private var onUpdate: (updates: List<Update>) -> Unit = { }
    private var onMessageSend: (message: Message) -> Unit = { }
    private var onStart: Bot.() -> Unit = { }
    private var onStop: Bot.(handler: CommandHandler?) -> Unit = { }

    // options
    private var blockingSelector: ((Update) -> Any?)? = null
    override val service = token?.let { TelegramApi.serviceWithToken(it) } ?: TelegramApi.service
    override val fileService = token?.let { TelegramApi.fileServiceWithToken(it) } ?: TelegramApi.fileService

    // states
    var isActive = false
        private set

    companion object {
        val logger: Logger = LoggerFactory.getLogger("tg-bot")

        /** Api call result of [getMe][TelegramService.getMe] method */
        @Deprecated("This constant returns the bot for a default service instance. Use the getMe() method from bot context", ReplaceWith("botContext { getMe() }"))
        val ME by lazy { TelegramApi.service.getMe().execute().body()?.result ?: errorBody() }

        internal var properties = setProperties()
        internal fun setProperties(): Properties? {
            return PropertiesFactory.fromOrNull("tgbot.properties") ?: PropertiesFactory.fromOrNull("bot.properties") // TODO [stable] remove second
        }
        
        internal var config = BotConfig()
    }
    
    private var needRefreshMe = false
    @get:JvmName("getMeProp")
    var me by MeDelegate(needRefreshMe)
        private set

    init {
        if (token != null) {
            config.token = token
            TelegramApi.setToken(token)
        }
    }
    
    /** Reloads config from properties file */
    fun reloadConfig() {
        properties = setProperties()
        needRefreshMe = true
    }
    
    /** Setup new bot config */
    fun config(setup: BotConfig.() -> Unit): Bot {
        config = BotConfig().apply(setup)
        needRefreshMe = true
        return this
    }

    fun enableBlocking(selector: (Update) -> Any?): Bot {
        blockingSelector = selector
        return this
    }

    private val queue = mutableMapOf<Any, Job>()

    private fun Update.withBlockingObject(action: (Any) -> Unit) {
        blockingSelector?.invoke(this)?.apply(action)
    }

    private lateinit var eventHandlerFactory: EventHandlerFactory
    private var additionalEventHandlers: MutableList<(eventHandlerFactory: EventHandlerFactory) -> Unit> = mutableListOf()

    fun Map<Any, Pair<UUID, Job>>?.data(key: Any) = this?.get(key).let { it?.first to it?.second }

    fun newUpdate(update: Update, service: TelegramService) = safeNetwork {
        val start = System.currentTimeMillis()

        var blockingTime = 0L
        update.withBlockingObject {
            runBlocking {
                val job = queue[it]
                if (job != null) {
                    blockingTime = measureTimeMillis {
                        logger.debug("Wait end blocking for {}...", it)
                        job.join()
                    }
                }
            }
        }

        try {
            scope.launch {
                try {
                    val handler = if (isLocationsMode) {
                        locationsWrapper!!.getHandlerFactory(update).apply {
                            
                            // TODO delete?
                            additionalEventHandlers.forEach {
                                apply(it)
                            }
                        }.getHandler(update, service, fileService)
                    } else {
                        eventHandlerFactory.getHandler(update, service, fileService)
                    }
                    
                    with(handler) {
                        if (this is CommandHandler && command.body == shutdownCommand) {
                            val chatId = update.message?.chat?.id?.value
                            val userId = update.message?.from?.id?.value
                            logger.info("Shutdown command was called from chat #${chatId} by user #${userId}")
                            logger.info("Shutting down bot...")
                            stop(this)
                            return@with
                        }
    
                        handle()
                    }
                } catch (e: Exception) {
                    safeOnError(e)
                } finally {
                    update.withBlockingObject {
                        queue.remove(it)
                    }

                    logger.debug(
                        "Update #${update.updateId} handled in ${System.currentTimeMillis() - start} ms${
                            if (blockingTime > 0) " (wait $blockingTime ms)" else ""
                        }."
                    )
                }
            }.also { job ->
                if (blockingSelector != null) {
                    update.withBlockingObject {
                        val uuid = UUID.randomUUID()
                        logger.debug("Add blocking for {} ({})", it, uuid)
                        queue[it] = job
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            update.withBlockingObject {
                logger.debug("Release blocking for {} after {} ms.", it, blockingTime)
                queue.remove(it)
            }
        }
    }
    
    private fun incLastUpdateId() {
        lastUpdateId = (lastUpdateId ?: 0) + 1
    }

    private fun safeNetwork(action: () -> Unit) {
        try {
            action()
        } catch (e: BotException) {
            incLastUpdateId()
            safeOnError(e)
            throw e
        } catch (e: IOException) {
            if (isActive) {
                safeOnError(e)
            }
        } catch (e: TelegramApiException) {
            safeOnError(e)
        } catch (e: Exception) {
            incLastUpdateId()
            safeOnError(e)
        }
    }

    private fun safeOnError(e: Exception) {
        try {
            onError(e)
        } catch (e: Exception) {
            logger.error("Exception while onError:", e)
        }
    }

    private var updateRequest: Call<NetworkResponse<List<Update>>>? = null
    private suspend fun startBot() {
        additionalEventHandlers.forEach {
            eventHandlerFactory.apply(it)
        }
        
        isActive = true
        logger.info("Tg bot started...")
        try {
            onStart(this)
    
            val allowedUpdates = if (isLocationsMode) {
                (eventHandlerFactory as LocationEventHandlerFactory<*>).locationsWrapper
                    .allowedUpdates().toList().asParameter()
            } else {
                eventHandlerFactory.allowedUpdates.toList().asParameter()
            }

            while (isActive) {
                safeNetwork {
                    updateRequest = service.getUpdates(
                        offset = lastUpdateId?.plus(1),
                        timeout = timeout,
                        allowedUpdates = allowedUpdates
                    )

                    val updates = updateRequest!!.execute()

                    if (updates.isSuccessful && updates.body()?.result?.isNotEmpty() == true) {
                        onUpdate(updates.body()!!.result)
                        updates.body()!!.result.forEach { update ->
                            newUpdate(update, service)
                        }

                        lastUpdateId = updates.body()!!.result.last().updateId
                    }
                }
            }
        } finally {
            stoppingJob?.join()
            logger.info("Bot stopped")
        }
    }

    /** Starts the bot after it has been stopped. Also stops the bot if it is active */
    suspend fun restart() {
        logger.info("Restarting...")
        if (isActive) {
            stop(null)
        }
        startBot()
    }

    // TODO убрать регистрацию обработчиков из EventHandlerFactory,
    //  поместить в этом классе, оставить start() без аргументов
    suspend fun start(updateHandler: BaseEventHandlerFactory.(bot: Bot) -> Unit) {
        eventHandlerFactory = BaseEventHandlerFactory()
        (eventHandlerFactory as BaseEventHandlerFactory).updateHandler(this)
        startBot()
    }
    
    private var isLocationsMode = false
    private var locationsWrapper: LocationsWrapper<*>? = null
    @LocationsDSL
    suspend fun <T : LocationConfig> locations(setup: LocationsWrapper<T>.() -> Unit) {
        locationsWrapper = LocationsWrapper<T>().apply(setup)
        eventHandlerFactory = LocationEventHandlerFactory(locationsWrapper as LocationsWrapper<*>)
        isLocationsMode = true
        startBot()
    }

    private var shutdownCommand: String? = null
    fun shutdownCommand(command: String): Bot {
        this.shutdownCommand = command.letIf({ it.startsWith("/") }) { it.drop(1) }
        return this
    }

    fun onError(onError: (e: Exception) -> Unit): Bot {
        this.onError = onError
        return this
    }

    fun onStop(onStop: Bot.(handler: CommandHandler?) -> Unit): Bot {
        this.onStop = onStop
        return this
    }


    fun onStart(onStart: Bot.() -> Unit): Bot {
        this.onStart = onStart
        return this
    }

    fun onUpdate(onUpdate: (updates: List<Update>) -> Unit): Bot {
        this.onUpdate = onUpdate
        return this
    }

    fun registerDatePicker(datePicker: DatePicker, alwaysAnswer: Boolean = config.alwaysAnswerCallback): Bot {
        additionalEventHandlers.add {
            when (it) {
                is BaseEventHandlerFactory -> {
                    it.handleCallbackQuery(alwaysAnswer, handlerId = datePicker.handlerId, datePicker = datePicker, handler = null)
                }
    
                is LocationEventHandlerFactory<*> -> {
                    it.handleCallbackQuery(alwaysAnswer, handlerId = datePicker.handlerId, datePicker = datePicker, handler = null)
                }
    
                else -> return@add
            }
        }
        return this
    }

    fun onMessageSend(onMessageSend: (message: Message) -> Unit): Bot {
        this.onMessageSend = onMessageSend
        return this
    }

    suspend fun stop(handler: CommandHandler? = null) {
        isActive = false
        updateRequest?.cancel()
        onStop(this, handler)
        logger.info("Waiting all processing updates...")
        scope.coroutineContext[Job]!!.children.forEach {
            it.cancelAndJoin()
        }
    }
}

