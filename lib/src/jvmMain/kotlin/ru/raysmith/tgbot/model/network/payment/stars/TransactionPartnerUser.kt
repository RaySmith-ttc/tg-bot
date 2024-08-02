package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Describes a transaction with a user. */
@Serializable
data class TransactionPartnerUser(

    /** Information about the user */
    @SerialName("user") val user: User,

    /** Bot-specified invoice payload */
    @SerialName("invoice_payload") val invoicePayload: String? = null,
) : TransactionPartner() {

    /** Type of the transaction partner, always “user” */
    override val type: String = "user"
}