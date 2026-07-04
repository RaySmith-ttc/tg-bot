package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The withdrawal failed and the transaction was refunded. */
@Serializable
data object RevenueWithdrawalStateFailed : RevenueWithdrawalState {

    /** Type of the state, always “failed” */
    @EncodeDefault
    @SerialName("type") override val type: String = "failed"
}
