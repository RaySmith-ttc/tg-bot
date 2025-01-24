package ru.raysmith.tgbot.model.network.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.Currency

/**
 * This object contains basic information about a successful payment.
 *
 * Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction,
 * the funds may be debited from your balance. This is outside of Telegram's control.
 * */
@Serializable
data class SuccessfulPayment(

    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code,
     * or [Currency.XTR] for payments in [Telegram Stars](https://t.me/BotNews/90)
     * */
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

    /** Expiration date of the subscription, in Unix time; for recurring payments only */
    @SerialName("subscription_expiration_date") val subscriptionExpirationDate: Int? = null,

    /** True, if the payment is a recurring payment for a subscription */
    @SerialName("is_recurring") val isRecurring: Boolean? = null,

    /** True, if the payment is the first payment for a subscription */
    @SerialName("is_first_recurring") val isFirstRecurring: Boolean? = null,

    /** Identifier of the shipping option chosen by the user */
    @SerialName("shipping_option_id") val shippingOptionId: String? = null,

    /** Order info provided by the user */
    @SerialName("order_info") val orderInfo: OrderInfo? = null,

    /** Telegram payment identifier */
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,

    /** Provider payment identifier */
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String
)