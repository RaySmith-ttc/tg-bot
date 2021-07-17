package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object contains basic information about an invoice. */
data class Invoice(

    /** Product name */
    @SerialName("title") val title: String,

    /** Product description */
    @SerialName("description") val description: String,

    /** Unique bot deep-linking parameter that can be used to generate this invoice */
    @SerialName("start_parameter") val startParameter: String,

    /**
     * Three-letter ISO 4217 currency code
     *
     * @see <a href="https://core.telegram.org/bots/payments#supported-currencies">Currency codes</a>
     * */
    @SerialName("currency") val currency: String,

    /** Total price in the *smallest units* of the currency (integer, **not** float/double).
     * For example, for a price of `US$ 1.45` pass `amount = 145`. See the *exp* parameter in currencies.json,
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     *
     * @see <a href="https://core.telegram.org/bots/payments/currencies.json">currencies.json</a>
     * */
    @SerialName("total_amount") val totalAmount: Int,
)
