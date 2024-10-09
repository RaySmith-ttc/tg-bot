package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.RevenueWithdrawalStateFailedSerializer

/** The withdrawal failed and the transaction was refunded. */
@Serializable(with = RevenueWithdrawalStateFailedSerializer::class)
data object RevenueWithdrawalStateFailed : RevenueWithdrawalState() {

    /** Type of the state, always “failed” */
    override val type: String = "failed"
}