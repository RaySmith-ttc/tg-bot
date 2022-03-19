package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import ru.raysmith.tgbot.core.handler.CommandHandler
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdatesResult
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.asParameter
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import kotlin.system.measureTimeMillis

private var stoppingJob: Job? = null
private var job = SupervisorJob()

// TODO impl string-length safe util (sendMessage -> text.take(4096))
class Bot(
    val token: String? = null,
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + job),
    var lastUpdateId: Int? = null,
) {

    // callbacks
    private var onError: (e: Exception) -> Unit = { }
    private var onShutdown: suspend () -> Unit = { }
    private var onUpdate: (updates: List<Update>) -> Unit = { }
    private var onMessageSend: (message: Message) -> Unit = { }
    private var onStop: () -> Unit = { }

    // options
    private var blockingSelector: ((Update) -> Any?)? = null
    val service = token?.let { TelegramApi.serviceWithToken(it) } ?: TelegramApi.service

    // states
    var isActive = false
    private var updateRequest: Call<UpdatesResult>? = null

    companion object {
        val logger: Logger = LoggerFactory.getLogger("tg-bot")

        /** Api call result of [getMe][TelegramService.getMe] method */
        val ME by lazy { TelegramApi.service.getMe().execute().body()!!.result }
    }

    init {
        if (token != null) {
            TelegramApi.setToken(token)
        }
    }

    fun enableBlocking(selector: (Update) -> Any?): Bot {
        blockingSelector = selector
        return this
    }

    fun onShutdown(onShutdown: suspend () -> Unit): Bot {
        this.onShutdown = onShutdown
        return this
    }


    private val queue = mutableMapOf<Any, Job>()

    private fun Update.withBlockingObject(action: (Any) -> Unit) {
        blockingSelector?.invoke(this)?.apply(action)
    }

    fun newUpdate(update: Update) = safeNetwork {
        val start = System.currentTimeMillis()

        var blockingTime = 0L
        update.withBlockingObject {
            runBlocking {
                val job = queue[it]
                if (job != null) {
                    blockingTime = measureTimeMillis {
                        job.join()
                    }
                }
            }
        }

        scope.launch {
            try {
                EventHandlerFactory.getHandler(update).run {
                    if (this is CommandHandler && command.body == shutdownCommand) {
                        val chatId = update.message?.chat?.id
                        val userId = update.message?.from?.id
                        logger.info("Shutdown command was called from chat #${chatId} by user #${userId}")
                        onShutdown()
                        logger.info("Shutting down bot...")
                        stop()
                        return@run
                    }

                    handle()
                }
            } catch (e: Exception) {
                safeOnError(e)
            } finally {
                update.withBlockingObject {
                    queue.remove(it)
                }

                logger.debug("Update #${update.updateId} handled in ${System.currentTimeMillis() - start} ms${
                    if (blockingTime > 0) " (wait $blockingTime ms)" else ""
                }.")
            }
        }.also { job ->
            if (blockingSelector != null) {
                update.withBlockingObject {
                    queue[it] = job
                }
            }
        }
    }

    private fun safeNetwork(action: () -> Unit) {
        try {
            action()
        } catch (e: BotException) {
            lastUpdateId = (lastUpdateId ?: 0) + 1
            safeOnError(e)
            throw e
        } catch (e: Exception) {
            lastUpdateId = (lastUpdateId ?: 0) + 1
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
        logger.info("Bot started")
        isActive = true

        try {
            while (isActive) {
                safeNetwork {
                    updateRequest = TelegramApi.service.getUpdates(
                        offset = lastUpdateId?.plus(1),
                        timeout = timeout,
                        allowedUpdates = EventHandlerFactory.getAllowedUpdateTypes().asParameter()
                    )

                    val updates = updateRequest!!.execute()

                    if (updates.isSuccessful && updates.body()?.result?.isNotEmpty() == true) {
                        onUpdate(updates.body()!!.result)
                        updates.body()!!.result.forEach { update ->
                            newUpdate(update)
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
            stop()
        }
        startBot()
    }

    // TODO убрать регистрацию обработчиков из EventHandlerFactory,
    //  поместить в этом классе, оставить start() без аргументов
    suspend fun start(updateHandler: EventHandlerFactory.() -> Unit) {
        EventHandlerFactory.updateHandler() // setup handlers
        startBot()
    }

    private var shutdownCommand: String? = null
    fun shutdownCommand(command: String): Bot {
        this.shutdownCommand = command
        return this
    }

    fun onError(onError: (e: Exception) -> Unit): Bot {
        this.onError = onError
        return this
    }

    fun onStop(onStop: () -> Unit): Bot {
        this.onStop = onStop
        return this
    }

    fun onUpdate(onUpdate: (updates: List<Update>) -> Unit): Bot {
        this.onUpdate = onUpdate
        return this
    }

    fun registerDatePicker(datePicker: DatePicker): Bot {
        EventHandlerFactory.handleCallbackQuery(handlerId = datePicker.handlerId, datePicker = datePicker)
        return this
    }

    fun onMessageSend(onMessageSend: (message: Message) -> Unit): Bot {
        this.onMessageSend = onMessageSend
        return this
    }

    suspend fun stop() {
        isActive = false
        updateRequest?.cancel()
        stoppingJob = scope.launch {
            logger.info("Waiting all processing updates...")
            job.children.forEach {
                it.cancelAndJoin()
            }

            logger.info("Done")
        }

        stoppingJob?.join()
        onStop()
    }
}

