package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery

@HandlerDsl
open class PreCheckoutQueryHandler(
    val preCheckoutQuery: PreCheckoutQuery,
    override val client: HttpClient,
    private val handler: suspend PreCheckoutQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<PreCheckoutQueryHandler> {

    override fun getChatId() = preCheckoutQuery.from.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return PreCheckoutQueryHandler(preCheckoutQuery, client, handler).let {
            this.block()
        }
    }

    suspend fun answerPreCheckoutQuery(ok: Boolean, errorMessage: String? = null): Boolean {
        return answerPreCheckoutQuery(preCheckoutQuery.id, ok, errorMessage)
    }
}