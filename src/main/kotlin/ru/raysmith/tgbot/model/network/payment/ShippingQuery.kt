package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
/** This object contains information about an incoming shipping query. */
data class ShippingQuery(
    /** Unique query identifier */
    @SerialName("id") val id: String,

    /** User who sent the query */
    @SerialName("from") val from: User,

    /** Bot specified invoice payload */
    @SerialName("invoice_payload") val invoicePayload: String,

    /** User specified shipping address */
    @SerialName("shipping_address") val shippingAddress: ShippingAddress,
)