package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The withdrawal succeeded. */
@Serializable
data class RevenueWithdrawalStateSucceeded(

    /** Date the withdrawal was completed in Unix time */
    @SerialName("date") val date: Int,

    /** An HTTPS URL that can be used to see transaction details */
    @SerialName("url") val url: String
) : RevenueWithdrawalState() {

    /** Type of the state, always “succeeded” */
    override val type: String = "succeeded"
}