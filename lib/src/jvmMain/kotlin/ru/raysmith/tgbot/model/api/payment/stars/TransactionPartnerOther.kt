package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a transaction with an unknown source or recipient. */
@Serializable
data object TransactionPartnerOther : TransactionPartner {

    /** Type of the transaction partner, always “other” */
    @EncodeDefault
    @SerialName("type") override val type: String = "other"
}
