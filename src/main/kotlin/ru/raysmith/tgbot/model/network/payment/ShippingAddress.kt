package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a shipping address. */
data class ShippingAddress(
    /** ISO 3166-1 alpha-2 country code */
    @SerialName("name") val name: String,

    /** State, if applicable */
    @SerialName("state") val state: String,

    /** City */
    @SerialName("city") val city: String,

    /** First line for the address */
    @SerialName("street_line1") val streetLine1: String,

    /** Second line for the address */
    @SerialName("street_line2") val streetLine2: String,

    /** Address post code */
    @SerialName("post_code") val postCode: String,
)