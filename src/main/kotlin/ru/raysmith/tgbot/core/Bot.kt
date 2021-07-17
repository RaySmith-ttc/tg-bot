package ru.raysmith.tgbot.core

import kotlinx.coroutines.*
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.asParameter

class Bot(
    val timeout: Int = 50,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
    var lastUpdateId: Int = 0,
) {

    private var onError: (e: Exception) -> Unit = { }

    private val exceptionHandler = (CoroutineExceptionHandler { _, throwable ->
        onError(throwable as Exception)
    })

    private suspend fun startBot() {
        while (scope.isActive) {
            try {
                val updates = TelegramApi.service.getUpdates(
                    offset = lastUpdateId + 1,
                    timeout = timeout,
                    allowedUpdates = EventHandlerFactory.getAllowedUpdateTypes().asParameter()
                ).execute()

                if (updates.isSuccessful && updates.body()!!.result.isNotEmpty()) {
                    updates.body()!!.result.forEach { update ->
                        scope.launch(exceptionHandler) {
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

    suspend fun start(updateHandler: EventHandlerFactory.() -> Unit) {
        EventHandlerFactory.updateHandler() // setup handlers
        startBot()
    }

    suspend fun onError(onError: (e: Exception) -> Unit): Bot {
        this.onError = onError
        return this
    }

    suspend fun stop(cause: CancellationException? = null) {
        scope.cancel(cause)
    }

}

