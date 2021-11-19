package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents information about an order. */
data class OrderInfo(
    /** User name */
    @SerialName("name") val name: String? = null,

    /** User's phone number */
    @SerialName("phone_number") val phoneNumber: String? = null,

    /** User email */
    @SerialName("email") val email: String? = null,

    /** User shipping address */
    @SerialName("shipping_address") val shippingAddress: ShippingAddress? = null,
)