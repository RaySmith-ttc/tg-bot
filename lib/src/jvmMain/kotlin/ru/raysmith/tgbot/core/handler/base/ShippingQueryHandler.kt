package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.BaseEventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.ShippingOption
import ru.raysmith.tgbot.model.network.payment.ShippingQuery

@HandlerDsl
open class ShippingQueryHandler(
    val shippingQuery: ShippingQuery,
    final override val bot: Bot,
    private val handler: suspend ShippingQueryHandler.() -> Unit = {}
) : BaseEventHandler(), BotContext<ShippingQueryHandler> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = shippingQuery.from.id
    override fun getChatIdOrThrow() = shippingQuery.from.id

    override suspend fun handle() {
        handler()
        handled = true
        handleLocalFeatures(handled)
    }

    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ShippingQueryHandler>.() -> R): R {
        return ShippingQueryHandler(shippingQuery, bot, handler).block()
    }

    suspend fun answerShippingQuery(ok: Boolean, shippingOptions: List<ShippingOption>, errorMessage: String? = null): Boolean {
        return answerShippingQuery(
            shippingQuery.id, ok, Json.encodeToString(shippingOptions), errorMessage
        )
    }
}