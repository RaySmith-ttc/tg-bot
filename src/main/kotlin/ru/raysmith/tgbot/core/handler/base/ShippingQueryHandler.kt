package ru.raysmith.tgbot.core.handler.base

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.model.network.payment.ShippingOption
import ru.raysmith.tgbot.model.network.payment.ShippingQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

@HandlerDsl
open class ShippingQueryHandler(
    val shippingQuery: ShippingQuery,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: ShippingQueryHandler.() -> Unit = {}
) : EventHandler, BotContext<ShippingQueryHandler> {

    override fun getChatId() = shippingQuery.from.id
    override fun getChatIdOrThrow() = shippingQuery.from.id
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override fun <R> withBot(bot: Bot, block: BotContext<ShippingQueryHandler>.() -> R): R {
        return ShippingQueryHandler(shippingQuery, bot.service, bot.fileService, handler).block()
    }

    fun answerShippingQuery(ok: Boolean, shippingOptions: List<ShippingOption>, errorMessage: String? = null): Boolean {
        return service.answerShippingQuery(
            shippingQuery.id, ok, Json.encodeToString(shippingOptions), errorMessage
        ).execute().body()?.result ?: errorBody()
    }
}