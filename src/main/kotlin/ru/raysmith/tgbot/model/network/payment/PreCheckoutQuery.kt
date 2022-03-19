package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.network.User

@Serializable
/** This object contains information about an incoming pre-checkout query. */
data class PreCheckoutQuery(
    /** Unique query identifier */
    @SerialName("id") val id: String,

    /** User who sent the query */
    @SerialName("from") val from: User,

    /** Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code */
    @SerialName("currency") val currency: Currency,

    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example,
     * for a price of `US$ 1.45` pass `amount = 145`. See the exp parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * */
    @SerialName("total_amount") val totalAmount: Int,

    /** Bot specified invoice payload */
    @SerialName("invoice_payload") val invoicePayload: String,

    /** Identifier of the shipping option chosen by the user */
    @SerialName("shipping_option_id") val shippingOptionId: String? = null,

    /** Order info provided by the user */
    @SerialName("order_info") val orderInfo: OrderInfo? = null,
) : ChatIdHolder {
    override fun getChatId() = from.id.toString()
}