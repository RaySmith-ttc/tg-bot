package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
open class EditedChannelPostHandler(
    val channelPost: Message,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: EditedChannelPostHandler.() -> Unit = {}
) : EventHandler, BotContext<EditedChannelPostHandler> {
    override fun getChatId() = channelPost.chat.id
    override fun getChatIdOrThrow() = channelPost.chat.id
    override var messageId: Int? = channelPost.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<EditedChannelPostHandler>.() -> R): R {
        return EditedChannelPostHandler(channelPost, bot.service, bot.fileService, handler).block()
    }
}