package ru.raysmith.tgbot.core.handler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.payment.ShippingOption
import ru.raysmith.tgbot.model.network.payment.ShippingQuery
import ru.raysmith.tgbot.network.TelegramApi

class ShippingQueryHandler(
    val shippingQuery: ShippingQuery,
    private val handler: ShippingQueryHandler.() -> Unit
) :  EventHandler, ISender, IEditor {

    override suspend fun handle() = handler()
    override var chatId: String? = shippingQuery.from.id.toString()
    override var messageId: Long? = null
    override var inlineMessageId: String? = null

    fun answerShippingQuery(ok: Boolean, shippingOptions: List<ShippingOption>, errorMessage: String? = null): Boolean {
        return TelegramApi.service.answerShippingQuery(
            shippingQuery.id, ok, Json.encodeToString(shippingOptions), errorMessage
        ).execute().body()!!.result
    }
}