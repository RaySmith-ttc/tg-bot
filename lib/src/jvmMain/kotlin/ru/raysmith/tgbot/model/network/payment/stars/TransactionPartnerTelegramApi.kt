package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a withdrawal transaction to the Telegram Ads platform. */
@Serializable
data class TransactionPartnerTelegramApi(

    /** The number of successful requests that exceeded regular limits and were therefore billed */
    @SerialName("request_count") val requestCount: Int
): TransactionPartner() {

    /** Type of the transaction partner, always “telegram_api” */
    override val type: String = "telegram_api"
}