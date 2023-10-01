package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class MessageHandler(
    val message: Message,
    override val client: HttpClient,
    private val handler: suspend MessageHandler.() -> Unit = { }
) : EventHandler, BotContext<MessageHandler> {

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id
    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageHandler>.() -> R): R {
        return MessageHandler(message, bot.client, handler).block()
    }
}