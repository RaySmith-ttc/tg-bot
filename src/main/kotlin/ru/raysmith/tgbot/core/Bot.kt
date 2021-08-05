package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.asParameter

class Bot(
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    var lastUpdateId: Int = 0,
) {

    private var onError: (e: Exception) -> Unit = { }
    private var onUpdate: (updates: List<Update>) -> Unit = { }
    private var onMessageSend: (message: Message) -> Unit = { }

    private suspend fun startBot() {
        while (scope.isActive) {
            try {

                @Suppress("BlockingMethodInNonBlockingContext")
                val updates = TelegramApi.service.getUpdates(
                    offset = lastUpdateId + 1,
                    timeout = timeout,
                    allowedUpdates = EventHandlerFactory.getAllowedUpdateTypes().asParameter()
                ).execute()

                if (updates.isSuccessful && updates.body()?.result?.isNotEmpty() == true) {
                    onUpdate(updates.body()!!.result)
                    updates.body()!!.result.forEach { update ->
                        scope.launch {
                            EventHandlerFactory.getHandler(update).handle()
                        }
                    }

                    lastUpdateId = updates.body()!!.result.last().updateId
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
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

    suspend fun onError(onError: (e: Exception) -> Unit): Bot {
        this.onError = onError
        return this
    }

    suspend fun onUpdate(onUpdate: (updates: List<Update>) -> Unit): Bot {
        this.onUpdate = onUpdate
        return this
    }

    suspend fun onMessageSend(onMessageSend: (message: Message) -> Unit): Bot {
        this.onMessageSend = onMessageSend
        return this
    }

    suspend fun stop(cause: CancellationException? = null) {
        scope.cancel(cause)
    }
}

