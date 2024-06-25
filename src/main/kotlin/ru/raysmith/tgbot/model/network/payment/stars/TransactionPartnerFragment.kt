package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a withdrawal transaction with Fragment. */
@Serializable
data class TransactionPartnerFragment(

    /** State of the transaction if the transaction is outgoing */
    @SerialName("withdrawal_state") val withdrawalState: RevenueWithdrawalState? = null
) : TransactionPartner() {

    /** Type of the transaction partner, always “fragment” */
    override val type: String = "fragment"
}

