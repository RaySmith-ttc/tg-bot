package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable

/** The withdrawal failed and the transaction was refunded. */
@Serializable
data object RevenueWithdrawalStateFailed : RevenueWithdrawalState() {

    /** Type of the state, always “failed” */
    override val type: String = "failed"
}