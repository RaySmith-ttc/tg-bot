package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class ChannelPostHandler(
    val channelPost: Message,
    final override val bot: Bot,
    private val handler: suspend ChannelPostHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<ChannelPostHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = channelPost.messageId
    override var inlineMessageId: String? = null

    override fun getChatId() = channelPost.chat.id
    override fun getChatIdOrThrow() = channelPost.chat.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChannelPostHandler>.() -> R): R {
        return ChannelPostHandler(channelPost, bot, handler).block()
    }
}

