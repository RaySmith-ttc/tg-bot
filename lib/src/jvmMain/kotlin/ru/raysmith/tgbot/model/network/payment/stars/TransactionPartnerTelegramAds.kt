package ru.raysmith.tgbot.model.network.payment.stars

import kotlinx.serialization.Serializable

/** Describes a withdrawal transaction to the Telegram Ads platform. */
@Serializable
data object TransactionPartnerTelegramAds: TransactionPartner() {

    /** Type of the transaction partner, always “telegram_ads” */
    override val type: String = "telegram_ads"
}