package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a withdrawal transaction with Fragment. */
@Serializable
data class TransactionPartnerFragment(

    /** State of the transaction if the transaction is outgoing */
    @SerialName("withdrawal_state") val withdrawalState: RevenueWithdrawalState? = null
) : TransactionPartner {

    /** Type of the transaction partner, always “fragment” */
    @EncodeDefault
    @SerialName("type") override val type: String = "fragment"
}

