package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.message.Message

@HandlerDsl
open class EditedChannelPostHandler(
    val channelPost: Message,
    override val client: HttpClient,
    private val handler: suspend EditedChannelPostHandler.() -> Unit = {}
) : EventHandler, BotContext<EditedChannelPostHandler> {
    override fun getChatId() = channelPost.chat.id
    override fun getChatIdOrThrow() = channelPost.chat.id
    override var messageId: Int? = channelPost.messageId
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedChannelPostHandler>.() -> R): R {
        return EditedChannelPostHandler(channelPost, client, handler).block()
    }
}