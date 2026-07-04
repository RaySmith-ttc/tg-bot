package ru.raysmith.tgbot.model.api.payment.stars

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a withdrawal transaction to the Telegram Ads platform. */
@Serializable // TODO test
data object TransactionPartnerTelegramAds: TransactionPartner {

    /** Type of the transaction partner, always “telegram_ads” */
    @EncodeDefault
    @SerialName("type") override val type: String = "telegram_ads"
}
