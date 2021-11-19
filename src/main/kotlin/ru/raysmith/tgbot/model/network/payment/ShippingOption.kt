package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents one shipping option.
 * */
@Serializable
data class ShippingOption(
    /** Shipping option identifier */
    @SerialName("id") val id: String,

    /** Option title */
    @SerialName("title") val title: String,

    /** List of price portions */
    @SerialName("prices") val prices: List<LabeledPrice>,
)
