package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import ru.raysmith.tgbot.core.handler.CommandHandler
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
import ru.raysmith.tgbot.utils.getOrDefault
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper
import ru.raysmith.tgbot.utils.locations.LocationsDSL
import ru.raysmith.utils.letIf
import ru.raysmith.utils.properties.PropertiesFactory
import ru.raysmith.utils.properties.getOrNull
import java.io.IOException
import java.util.*
import kotlin.system.measureTimeMillis

// TODO impl string-length safe util (sendMessage -> text.take(4096))
class Bot(
    val token: String? = null,
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    var lastUpdateId: Int? = null,
) : ApiCaller {

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
    private var updateRequest: Call<NetworkResponse<List<Update>>>? = null

    companion object {
        val logger: Logger = LoggerFactory.getLogger("tg-bot")

        /** Api call result of [getMe][TelegramService.getMe] method */
        @Deprecated("This constant returns the bot for a default service instance. Use the getMe() method from bot context", ReplaceWith("botContext { getMe() }"))
        val ME by lazy { TelegramApi.service.getMe().execute().body()?.result ?: errorBody() }

        val properties = PropertiesFactory.fromOrNull("bot.properties")
    }

    object Config {
        val safeTextLength = properties.getOrDefault("safeTextLength", "true").toBoolean()
        val printNulls = properties.getOrDefault("printNulls", "false").toBoolean()
        val defaultProviderToken = properties?.getOrNull("providerToken")
        val emptyCallbackQuery = properties.getOrDefault("emptyCallbackQuery", " ")
        val token = properties?.getOrNull("token") ?: System.getenv("TG_BOT_TOKEN") ?: System.getenv("BOT_TOKEN")

        val defaultRows: Int = properties.getOrDefault("pagination.rows", "5").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.rows is not Int")
        val defaultColumns: Int = properties.getOrDefault("pagination.columns", "1").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.columns is not Int")
        val firstPageSymbol = properties.getOrDefault("pagination.firstPageSymbol", "«")
        val lastPageSymbol = properties.getOrDefault("pagination.lastPageSymbol", "»")
        val locale = properties?.getOrNull("calendar_locale")?.let {
            Locale.forLanguageTag(it)
        } ?: Locale.getDefault()

        fun init() {
            // init fields
        }
    }

    init {
        Config.init()
        if (token != null) {
            TelegramApi.setToken(token)
        }
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
                        logger.debug("Wait end blocking for $it...")
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
                        logger.debug("Add blocking for $it ($uuid)")
                        queue[it] = job
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            update.withBlockingObject {
                logger.debug("Release blocking for $it after $blockingTime ms.")
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

    private suspend fun startBot() {
        additionalEventHandlers.forEach {
            eventHandlerFactory.apply(it)
        }
        
        isActive = true
        logger.info("Tg bot started...")
        try {
            onStart(this)
    
            val allowedUpdates = if (isLocationsMode) {
                val loc = locationsWrapper!!.locations.map { it.value.handlerFactory.allowedUpdates }.flatten().toSet()
                val additional = eventHandlerFactory.allowedUpdates
                (loc + additional).toList().asParameter()
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
    suspend fun start(updateHandler: EventHandlerFactoryImpl.(bot: Bot) -> Unit) {
        eventHandlerFactory = EventHandlerFactoryImpl()
        (eventHandlerFactory as EventHandlerFactoryImpl).updateHandler(this)
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

    fun registerDatePicker(datePicker: DatePicker): Bot {
        additionalEventHandlers.add {
            when (it) {
                is EventHandlerFactoryImpl -> {
                    it.handleCallbackQuery(handlerId = datePicker.handlerId, datePicker = datePicker, handler = null)
                }
    
                is LocationEventHandlerFactory<*> -> {
                    it.handleCallbackQuery(handlerId = datePicker.handlerId, datePicker = datePicker, handler = null)
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

