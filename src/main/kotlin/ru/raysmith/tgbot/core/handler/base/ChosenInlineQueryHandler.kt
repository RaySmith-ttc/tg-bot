package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.ChosenInlineResult

@HandlerDsl
open class ChosenInlineQueryHandler(
    val inlineResult: ChosenInlineResult,
    override val client: HttpClient,
    private val handler: suspend ChosenInlineQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<ChosenInlineQueryHandler> {
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = inlineResult.from.id
    override fun getChatIdOrThrow() = inlineResult.from.id
    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChosenInlineQueryHandler>.() -> R): R {
        return ChosenInlineQueryHandler(inlineResult, bot.client, handler).block()
    }
}