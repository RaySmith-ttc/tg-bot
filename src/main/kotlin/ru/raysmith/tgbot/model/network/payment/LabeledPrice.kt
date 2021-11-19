package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a portion of the price for goods or services. */
data class LabeledPrice(

    /** Portion label */
    @SerialName("label") val label: String,

    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example,
     * for a price of `US$ 1.45` pass `amount = 145`. See the exp parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * */
    @SerialName("amount") val amount: Int,
)
