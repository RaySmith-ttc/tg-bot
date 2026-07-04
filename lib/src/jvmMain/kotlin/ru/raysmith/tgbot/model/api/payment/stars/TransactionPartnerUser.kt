package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.gift.Gift
import ru.raysmith.tgbot.model.api.media.paid.PaidMedia

/** Describes a transaction with a user. */
@Serializable
data class TransactionPartnerUser(

    /** Type of the transaction */
    @SerialName("transaction_type") val transactionType: TransactionType,

    /** Information about the user */
    @SerialName("user") val user: User,

    /**
     * Information about the affiliate that received a commission via this transaction. Can be available only for
     * [TransactionType.INVOICE_PAYMENT] and [TransactionType.PAID_MEDIA_PAYMENT] transactions.
     * */
    @SerialName("affiliate") val affiliate: AffiliateInfo? = null,

    /** Bot-specified invoice payload. Can be available only for [TransactionType.INVOICE_PAYMENT] transactions. */
    @SerialName("invoice_payload") val invoicePayload: String? = null,

    /**
     * The duration of the paid subscription. Can be available only for [TransactionType.INVOICE_PAYMENT] transactions.
     * */
    @SerialName("subscription_period") val subscriptionPeriod: Int? = null,

    /**
     * Information about the paid media bought by the user; for [TransactionType.PAID_MEDIA_PAYMENT] transactions only
     * */
    @SerialName("paid_media") val paidMedia: List<PaidMedia>? = null,

    /**
     * Bot-specified paid media payload. Can be available only for [TransactionType.PAID_MEDIA_PAYMENT] transactions.
     * */
    @SerialName("paid_media_payload") val paidMediaPayload: String? = null,

    /** The gift sent to the user by the bot; for [TransactionType.GIFT_PURCHASE] transactions only */
    @SerialName("gift") val gift: Gift? = null,

    /**
     * Number of months the gifted Telegram Premium subscription will be active for; for
     * [TransactionType.PREMIUM_PURCHASE] transactions only
     * */
    @SerialName("premium_subscription_duration") val premiumSubscriptionDuration: Int? = null,
) : TransactionPartner {

    /** Type of the transaction partner, always “user” */
    @EncodeDefault
    @SerialName("type") override val type: String = "user"
}

