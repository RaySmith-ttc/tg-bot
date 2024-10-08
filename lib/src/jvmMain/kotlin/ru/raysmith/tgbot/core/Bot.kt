package ru.raysmith.tgbot.core

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.handler.BaseEventHandlerFactory
import ru.raysmith.tgbot.core.handler.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.base.CommandHandler
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramApiException
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsDSL
import ru.raysmith.tgbot.utils.locations.LocationsWrapper
import ru.raysmith.utils.letIf
import ru.raysmith.utils.properties.PropertiesFactory
import java.io.IOException
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * Creates bot instance
 *
 * @param token Bot token from [@BotFather](https://t.me/botfather).
 * Can be null if is set via environment variable or properties
 * @param timeout Timeout in seconds for long polling. Defaults to 50.
 * Should be positive, short polling should be used for testing purposes only.
 * @param scope Coroutine scope for new updates
 * @param lastUpdateId Identifier of the first update to be returned. Must be greater by one than the highest among the
 * identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update
 * are returned. An update is considered confirmed as soon as [getUpdates] is called with an *offset* higher than
 * its *update_id*. The negative offset can be specified to retrieve updates starting from -*offset* update from
 * the end of the updates queue. All previous updates will be forgotten.
 * */
class Bot(
    val token: String? = null,
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    private var lastUpdateId: Int? = null,
) : API {

    init {
        check(timeout >= 0) { "timeout should be positive" }
    }

    private var stoppingJob: Job? = null

    // callbacks
    private var onError: suspend (e: Exception) -> Unit = { }
    private var onUpdate: suspend (updates: List<Update>) -> Unit = { }
    private var onStart: suspend Bot.() -> Unit = { }
    private var onStop: suspend Bot.(handler: CommandHandler?) -> Unit = { }

    // options
    private var blockingSelector: ((Update) -> Any?)? = null
    private var clientBuilder: HttpClientConfig<OkHttpConfig>.() -> Unit = {}
    override val client: HttpClient by lazy {
        token?.let { TelegramApi.defaultClient(it, clientBuilder) }
            ?: TelegramApi.defaultClient(builder = clientBuilder)
    }

    // states
    var isActive = false
        private set

    companion object {
        val logger: Logger = LoggerFactory.getLogger("tg-bot")

        var properties = getProperties()
            private set

        @JvmName("getPropertiesFromFile")
        internal fun getProperties(): Properties? {
            return PropertiesFactory.fromOrNull("tgbot.properties") ?: PropertiesFactory.fromOrNull("bot.properties") // TODO [stable] remove second
        }
    }

    override var botConfig = BotConfig()
        private set
    
    private var needRefreshMe = false
    /** Api call result of [getMe][API.getMe] method */
    @get:JvmName("getMeProp")
    val me by MeDelegate(needRefreshMe) { needRefreshMe = it }

    init {
        if (token != null) {
            botConfig.token = token
        }
    }
    
    /** Reloads config from properties file */
    fun reloadConfig() {
        properties = getProperties()
        botConfig = BotConfig()
        needRefreshMe = true
    }
    
    /** Setup new bot config */
    fun config(setup: BotConfig.() -> Unit): Bot {
        botConfig = BotConfig().apply(setup)
        needRefreshMe = true
        return this
    }

    fun enableBlocking(selector: (Update) -> Any?): Bot {
        blockingSelector = selector
        return this
    }

    private val queue = Collections.synchronizedMap(mutableMapOf<Any, Job>())

    private fun Update.withBlockingObject(action: (Any) -> Unit) {
        blockingSelector?.invoke(this)?.apply(action)
    }

    private lateinit var eventHandlerBuilder: () -> Unit
    private lateinit var eventHandlerFactory: EventHandlerFactory
    private var additionalEventHandlers: MutableList<(eventHandlerFactory: EventHandlerFactory) -> Unit> = mutableListOf()

    fun Map<Any, Pair<UUID, Job>>?.data(key: Any) = this?.get(key).let { it?.first to it?.second }

    suspend fun newUpdate(update: Update) = safeNetwork {
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
                    logger.debug("Release blocking for {} after {} ms.", it, blockingTime)
                }
            }
        }

        try {
            scope.launch {
                try {
                    val handler = if (isLocationsMode) {
                        locationsWrapper!!.getHandlerFactory(update, additionalEventHandlers).getHandler(update)
                    } else {
                        eventHandlerFactory.getHandler(update)
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
                        logger.debug("Release blocking for {} after {} ms.", it, blockingTime)
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

    private suspend inline fun safeNetwork(action: () -> Unit) {
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

    private suspend fun safeOnError(e: Exception) {
        try {
            onError(e)
        } catch (e: Exception) {
            logger.error("Exception while onError:", e)
        }
    }

    private val updateScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private suspend fun startBot() {
        eventHandlerBuilder()
        additionalEventHandlers.forEach {
            eventHandlerFactory.apply(it)
        }
        
        isActive = true
        logger.info("Tg bot started...")
        try {
            onStart(this)
    
            val allowedUpdates = if (isLocationsMode) {
                (eventHandlerFactory as LocationEventHandlerFactory<*>).locationsWrapper
                    .allowedUpdates(this).toList()
            } else {
                eventHandlerFactory.allowedUpdates.toList()
            }

            while (isActive) {
                safeNetwork {
                    updateScope.async {
                        val updates = try {
                            getUpdates(
                                offset = lastUpdateId,
                                timeout = timeout,
                                allowedUpdates = allowedUpdates
                            )
                        } catch (e: Exception) {
                            safeOnError(e)
                            return@async
                        }

                        if (updates.isNotEmpty()) {
                            onUpdate(updates)
                            updates.forEach { update ->
                                newUpdate(update)
                            }

                            lastUpdateId = updates.last().updateId + 1
                        }
                    }.await()
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

    suspend fun start(updateHandler: BaseEventHandlerFactory.(bot: Bot) -> Unit) {
        eventHandlerFactory = BaseEventHandlerFactory(this)
        eventHandlerBuilder = {
            (eventHandlerFactory as BaseEventHandlerFactory).updateHandler(this)
        }

        startBot()
    }
    
    private var isLocationsMode = false
    private var locationsWrapper: LocationsWrapper<*>? = null
    @LocationsDSL
    suspend fun <T : LocationConfig> locations(setup: suspend LocationsWrapper<T>.() -> Unit) {
        locationsWrapper = LocationsWrapper<T>(this).apply { setup() }
        eventHandlerFactory = LocationEventHandlerFactory(locationsWrapper as LocationsWrapper<*>)
        eventHandlerBuilder = {}
        isLocationsMode = true

        startBot()
    }

    private var shutdownCommand: String? = null

    /** Sets shutdown [command]. After calling the [command] bot will shut down. This can be handled in [Bot.onStop] */
    fun shutdownCommand(command: String): Bot {
        this.shutdownCommand = command.letIf({ it.startsWith("/") }) { it.drop(1) }
        return this
    }

    /** Calling when the bot catch any exception */
    fun onError(onError: suspend (e: Exception) -> Unit): Bot {
        this.onError = onError
        return this
    }

    /** Called when the bot stops working */
    fun onStop(onStop: suspend Bot.(handler: CommandHandler?) -> Unit): Bot {
        this.onStop = onStop
        return this
    }

    /** Calling when the bot start */
    fun onStart(onStart: suspend Bot.() -> Unit): Bot {
        this.onStart = onStart
        return this
    }

    /** Calling when the bot get new update from long pool */
    fun onUpdate(onUpdate: suspend (updates: List<Update>) -> Unit): Bot {
        this.onUpdate = onUpdate
        return this
    }

    fun registerDatePicker(datePicker: DatePicker, alwaysAnswer: Boolean = botConfig.alwaysAnswerCallback): Bot {
        additionalEventHandlers.add {
            when (it) {
                is BaseEventHandlerFactory -> {
                    it.handleCallbackQuery(alwaysAnswer, handler = { setupFeatures(datePicker, callFirst = true) })
                }
    
                is LocationEventHandlerFactory<*> -> {
                    it.handleCallbackQuery(alwaysAnswer, handler = { setupFeatures(datePicker, callFirst = true) })
                }
    
                else -> return@add
            }
        }
        return this
    }

    /**
     * Stops bot working.
     * @param handler CommandHandler that used for stopping with [shutdownCommand] or null
     * @param force if true new updates and current handling will be finished
     * */
    suspend fun stop(handler: CommandHandler? = null, force: Boolean = false) {
        isActive = false
        updateScope.coroutineContext[Job]!!.children.forEach {
            if (force) {
                it.cancel()
            } else {
                it.cancelAndJoin()
            }
        }
        onStop(this, handler)
        logger.info("Waiting all processing updates...")
        scope.coroutineContext[Job]!!.children.forEach {
            if (force) {
                it.cancel()
            } else {
                it.cancelAndJoin()
            }
        }
    }

    fun client(clientBuilder: HttpClientConfig<OkHttpConfig>.() -> Unit): Bot {
        this.clientBuilder = clientBuilder
        return this
    }
}

