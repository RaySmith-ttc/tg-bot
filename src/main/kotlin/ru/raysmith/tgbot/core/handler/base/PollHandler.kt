package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.Poll

@HandlerDsl
open class PollHandler(
    val poll: Poll,
    override val client: HttpClient,
    private val handler: suspend PollHandler.() -> Unit = {}
) : EventHandler, BotContext<PollHandler> {

    override fun getChatId() = null
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollHandler>.() -> R): R {
        return PollHandler(poll, bot.client, handler).block()
    }
}

