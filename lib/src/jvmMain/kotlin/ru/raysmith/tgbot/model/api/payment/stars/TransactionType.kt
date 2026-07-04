package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Type of the transaction
 *
 * - [INVOICE_PAYMENT] - Payments via invoices
 * - [PAID_MEDIA_PAYMENT] - Payments for paid media
 * - [GIFT_PURCHASE] - Gifts sent by the bot
 * - [PREMIUM_PURCHASE] - Telegram Premium subscriptions gifted by the bot
 * - [BUSINESS_ACCOUNT_TRANSFER] - Direct transfers from managed business accounts
 * */
@Serializable
enum class TransactionType {

    /** Payments via invoices */
    @SerialName("invoice_payment") INVOICE_PAYMENT,

    /** Payments for paid media */
    @SerialName("paid_media_payment") PAID_MEDIA_PAYMENT,

    /** Gifts sent by the bot */
    @SerialName("gift_purchase") GIFT_PURCHASE,

    /** Telegram Premium subscriptions gifted by the bot */
    @SerialName("premium_purchase") PREMIUM_PURCHASE,

    /** Direct transfers from managed business accounts */
    @SerialName("business_account_transfer") BUSINESS_ACCOUNT_TRANSFER,
}
