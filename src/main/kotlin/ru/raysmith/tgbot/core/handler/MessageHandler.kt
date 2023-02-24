package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class MessageHandler(
    val message: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: MessageHandler.() -> Unit
) : EventHandler, BotContext<MessageHandler> {

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun withBot(bot: Bot, block: BotContext<MessageHandler>.() -> Any) {
        MessageHandler(message, service, fileService, handler).apply {
            this.block()
        }
    }
}