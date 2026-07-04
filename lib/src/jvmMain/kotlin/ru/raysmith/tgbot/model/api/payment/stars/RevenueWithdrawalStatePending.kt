package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The withdrawal is in progress. */
@Serializable
data object RevenueWithdrawalStatePending : RevenueWithdrawalState {

    /** Type of the state, always “pending” */
    @EncodeDefault
    @SerialName("type") override val type: String = "pending"
}
