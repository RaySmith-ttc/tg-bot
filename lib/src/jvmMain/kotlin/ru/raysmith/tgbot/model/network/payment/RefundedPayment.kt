package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency

/**
 * This object contains basic information about a refunded payment.
 */
@Serializable
data class RefundedPayment(

    /**
     * [Currency] for payments. Currently, always [Currency.XTR].
     */
    @SerialName("currency")
    val currency: Currency,

    /**
     * Total refunded price in the *smallest units* of the currency (integer, **not** float/double).
     * For example, for a price of `US$ 1.45`, `total_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    @SerialName("total_amount")
    val totalAmount: Int,

    /**
     * Bot-specified invoice payload.
     */
    @SerialName("invoice_payload")
    val invoicePayload: String,

    /**
     * Telegram payment identifier.
     */
    @SerialName("telegram_payment_charge_id")
    val telegramPaymentChargeId: String,

    /**
     * *Optional*. Provider payment identifier.
     */
    @SerialName("provider_payment_charge_id")
    val providerPaymentChargeId: String? = null
)