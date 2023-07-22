package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class ChannelPostHandler(
    val channelPost: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: ChannelPostHandler.() -> Unit = {}
) : EventHandler, BotContext<ChannelPostHandler> {
    override fun getChatId() = channelPost.chat.id
    override fun getChatIdOrThrow() = channelPost.chat.id
    override var messageId: Int? = channelPost.messageId
    override var inlineMessageId: String? = null
    
    override suspend fun handle() = handler()
    
    override fun <R> withBot(bot: Bot, block: BotContext<ChannelPostHandler>.() -> R): R {
        return ChannelPostHandler(channelPost, bot.service, bot.fileService, handler).block()
    }
}

