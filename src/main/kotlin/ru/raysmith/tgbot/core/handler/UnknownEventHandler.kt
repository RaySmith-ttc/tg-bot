package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
class UnknownEventHandler(val update: Update, val handler: UnknownEventHandler.() -> Unit = {}) : EventHandler {
    override suspend fun handle() = handler()
    override fun getChatId() = update.findChatId()

    override var service: TelegramService = TelegramApi.service
    fun withService(service: TelegramService, block: UnknownEventHandler.() -> Any) {
        UnknownEventHandler(update, handler).apply {
            this.service = service
            this.block()
        }
    }

    override var messageId = update.message?.messageId
    override var inlineMessageId = update.callbackQuery?.inlineMessageId
}