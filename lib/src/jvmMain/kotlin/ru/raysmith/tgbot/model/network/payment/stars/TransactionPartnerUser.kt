package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.gift.Gift
import ru.raysmith.tgbot.model.network.media.paid.PaidMedia

/** Describes a transaction with a user. */
@Serializable
data class TransactionPartnerUser(

    /** Information about the user */
    @SerialName("user") val user: User,

    /** Information about the affiliate that received a commission via this transaction */
    @SerialName("affiliate") val affiliate: AffiliateInfo? = null,

    /** Bot-specified invoice payload */
    @SerialName("invoice_payload") val invoicePayload: String? = null,

    /** The duration of the paid subscription */
    @SerialName("subscription_period") val subscriptionPeriod: Int? = null,

    /** Information about the paid media bought by the user */
    @SerialName("paid_media") val paidMedia: List<PaidMedia>? = null,

    /** Bot-specified paid media payload */
    @SerialName("paid_media_payload") val paidMediaPayload: String? = null,

    /** The gift sent to the user by the bot */
    @SerialName("gift") val gift: Gift? = null,
) : TransactionPartner() {

    /** Type of the transaction partner, always “user” */
    override val type: String = "user"
}