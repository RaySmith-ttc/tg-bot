package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.PreCheckoutQuery

@HandlerDsl
open class PreCheckoutQueryHandler(
    val preCheckoutQuery: PreCheckoutQuery,
    final override val bot: Bot,
    private val handler: suspend PreCheckoutQueryHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<PreCheckoutQueryHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = preCheckoutQuery.from.id
    override fun getChatIdOrThrow() = preCheckoutQuery.from.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return PreCheckoutQueryHandler(preCheckoutQuery, bot, handler).let {
            this.block()
        }
    }

    suspend fun answerPreCheckoutQuery(ok: Boolean, errorMessage: String? = null): Boolean {
        return answerPreCheckoutQuery(preCheckoutQuery.id, ok, errorMessage)
    }
}