package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.RevenueWithdrawalStatePendingSerializer

/** The withdrawal is in progress. */
@Serializable(with = RevenueWithdrawalStatePendingSerializer::class)
data object RevenueWithdrawalStatePending : RevenueWithdrawalState() {

    /** Type of the state, always “pending” */
    override val type: String = "pending"
}