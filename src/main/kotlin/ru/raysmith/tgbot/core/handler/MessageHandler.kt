package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class MessageHandler(val message: Message, private val handler: MessageHandler.() -> Unit) : EventHandler {

    override fun getChatId() = message.chat.id.toString()
    override var messageId: Long? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override var service: TelegramService = TelegramApi.service
    fun withService(service: TelegramService, block: MessageHandler.() -> Any) {
        MessageHandler(message, handler).apply {
            this.service = service
            this.block()
        }
    }
}