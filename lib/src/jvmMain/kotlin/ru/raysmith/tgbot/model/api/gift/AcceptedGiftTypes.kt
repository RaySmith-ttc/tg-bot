package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the types of gifts that can be gifted to a user or a chat. */
@Serializable
data class AcceptedGiftTypes(

    /** *True*, if unlimited regular gifts are accepted */
    @SerialName("unlimited_gifts") val unlimitedGifts: Boolean,

    /** *True*, if limited regular gifts are accepted */
    @SerialName("limited_gifts") val limitedGifts: Boolean,

    /** *True*, if unique gifts or gifts that can be upgraded to unique for free are accepted */
    @SerialName("unique_gifts") val uniqueGifts: Boolean,

    /** *True*, if a Telegram Premium subscription is accepted */
    @SerialName("premium_subscription") val premiumSubscription: Boolean,

    /** *True*, if transfers of unique gifts from channels are accepted */
    @SerialName("gifts_from_channels") val giftsFromChannels: Boolean,
)
