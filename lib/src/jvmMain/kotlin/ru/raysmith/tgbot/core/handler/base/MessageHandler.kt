package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class MessageHandler(
    val message: Message,
    final override val bot: Bot,
    private val handler: suspend MessageHandler.() -> Unit = { }
) : BaseEventHandler(), BotContext<MessageHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = message.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = message.chat.id
    override fun getChatIdOrThrow() = message.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<MessageHandler>.() -> R): R {
        return MessageHandler(message, bot, handler).block()
    }
}