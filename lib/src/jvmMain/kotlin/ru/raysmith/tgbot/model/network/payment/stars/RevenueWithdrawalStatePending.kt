package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable

/** The withdrawal is in progress. */
@Serializable
data object RevenueWithdrawalStatePending : RevenueWithdrawalState() {

    /** Type of the state, always “pending” */
    override val type: String = "pending"
}