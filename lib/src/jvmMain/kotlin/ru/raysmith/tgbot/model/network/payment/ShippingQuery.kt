package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.network.User

/** This object contains information about an incoming shipping query. */
@Serializable
data class ShippingQuery(
    /** Unique query identifier */
    @SerialName("id") val id: String,

    /** User who sent the query */
    @SerialName("from") val from: User,

    /** Bot specified invoice payload */
    @SerialName("invoice_payload") val invoicePayload: String,

    /** User specified shipping address */
    @SerialName("shipping_address") val shippingAddress: ShippingAddress,
) : ChatIdHolder {
    override fun getChatId() = from.id
    override fun getChatIdOrThrow() = from.id
}