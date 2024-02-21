package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.inline.InlineQuery
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultsButton
import kotlin.time.Duration

@HandlerDsl
open class InlineQueryHandler(
    val inlineQuery: InlineQuery,
    override val client: HttpClient,
    private val handler: suspend InlineQueryHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<InlineQueryHandler> {
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = inlineQuery.from.id
    override fun getChatIdOrThrow() = inlineQuery.from.id
    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    /**
     * @see answerInlineQuery
     * */
    suspend fun answer(
        id: String, results: List<InlineQueryResult>, cacheTime: Duration? = null, isPersonal: Boolean? = null,
        nextOffset: String? = null, button: InlineQueryResultsButton? = null
    ) = answerInlineQuery(id, results, cacheTime, isPersonal, nextOffset, button)

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<InlineQueryHandler>.() -> R): R {
        return InlineQueryHandler(inlineQuery, bot.client, handler).block()
    }
}

