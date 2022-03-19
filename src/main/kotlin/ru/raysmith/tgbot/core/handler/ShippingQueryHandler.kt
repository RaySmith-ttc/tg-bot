package ru.raysmith.tgbot.core.handler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.payment.ShippingOption
import ru.raysmith.tgbot.model.network.payment.ShippingQuery
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

@HandlerDsl
class ShippingQueryHandler(
    val shippingQuery: ShippingQuery,
    private val handler: ShippingQueryHandler.() -> Unit
) : EventHandler {

    override fun getChatId() = shippingQuery.from.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null

    override suspend fun handle() = handler()

    override var service: TelegramService = TelegramApi.service
    fun withService(service: TelegramService, block: ShippingQueryHandler.() -> Any) {
        ShippingQueryHandler(shippingQuery, handler).apply {
            this.service = service
            this.block()
        }
    }

    fun answerShippingQuery(ok: Boolean, shippingOptions: List<ShippingOption>, errorMessage: String? = null): Boolean {
        return TelegramApi.service.answerShippingQuery(
            shippingQuery.id, ok, Json.encodeToString(shippingOptions), errorMessage
        ).execute().body()!!.result
    }
}