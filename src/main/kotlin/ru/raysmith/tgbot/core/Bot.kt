package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.asParameter
import ru.raysmith.tgbot.utils.datepicker.DatePicker

private val job = SupervisorJob()

class Bot(
    val token: String? = null,
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + job),
    var lastUpdateId: Int = 0,
) {

    private var onError: (e: Exception) -> Unit = { }
    private var onShutdown: suspend () -> Unit = { }
    private var onUpdate: (updates: List<Update>) -> Unit = { }
    private var onMessageSend: (message: Message) -> Unit = { }

    companion object {
        val logger = LoggerFactory.getLogger("bot")
        val ME by lazy { TelegramApi.service.getMe().execute().body()!!.result }
    }

    init {
        if (token != null) {
            TelegramApi.setToken(token)
        }
    }

    fun onShutdown(onShutdown: suspend () -> Unit): Bot {
        this.onShutdown = onShutdown
        return this
    }

    fun newUpdate(update: Update) = processNetwork {
        scope.launch {
            EventHandlerFactory.getHandler(update).run {
                if (this is CommandHandler && command.text == shutdownCommand) {
                    logger.info("Shutdown command was called")
                    onShutdown()
                    logger.info("Shutting down bot...")
                    job.cancelAndJoin()
                    return@run
                }

                handle()
            }
        }
    }

    private fun processNetwork(action: () -> Unit) {
        try {
            action()
        } catch (e: BotException) {
            try { onError(e) } catch (e: Exception) { }
            throw e
        } catch (e: Exception) {
            try {
                onError(e)
            } catch (e: Exception) {
                logger.error("Exception while onError:", e)
            }
        }
    }

    private suspend fun startBot() {
        while (scope.isActive) {
            processNetwork {
                @Suppress("BlockingMethodInNonBlockingContext")
                val updates = TelegramApi.service.getUpdates(
                    offset = lastUpdateId + 1,
                    timeout = timeout,
                    allowedUpdates = EventHandlerFactory.getAllowedUpdateTypes().asParameter()
                ).execute()

                if (updates.isSuccessful && updates.body()?.result?.isNotEmpty() == true) {
                    onUpdate(updates.body()!!.result)
                    updates.body()!!.result.forEach { update ->
                        newUpdate(update)
                    }

                    lastUpdateId = updates.body()!!.result.last().updateId
                }
            }
        }

        logger.info("Bot stopped")
    }

    suspend fun restart() {
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

    fun stop(cause: CancellationException? = null) {
        scope.cancel(cause)
    }
}

