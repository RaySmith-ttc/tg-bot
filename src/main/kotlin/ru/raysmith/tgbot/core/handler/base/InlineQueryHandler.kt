package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.inline.InlineQuery
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultsButton
import kotlin.time.Duration

@HandlerDsl
open class InlineQueryHandler(
    val inlineQuery: InlineQuery,
    override val client: HttpClient,
    private val handler: InlineQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<InlineQueryHandler> {
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = inlineQuery.from.id
    override fun getChatIdOrThrow() = inlineQuery.from.id
    override fun handle() = handler()

    /**
     * @see answerInlineQuery
     * */
    fun answer(
        id: String, results: List<InlineQueryResult>, cacheTime: Duration? = null, isPersonal: Boolean? = null,
        nextOffset: String? = null, button: InlineQueryResultsButton? = null
    ) = answerInlineQuery(id, results, cacheTime, isPersonal, nextOffset, button)

    override fun <R> withBot(bot: Bot, block: BotContext<InlineQueryHandler>.() -> R): R {
        return InlineQueryHandler(inlineQuery, bot.client, handler).block()
    }
}

