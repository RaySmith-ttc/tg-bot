package ru.raysmith.tgbot.model.network.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.sticker.Sticker

/** This object represents a gift that can be sent by the bot. */
@Serializable
data class Gift(

    /** Unique identifier of the gift */
    @SerialName("id") val id: String,

    /** The sticker that represents the gift */
    @SerialName("sticker") val sticker: Sticker,

    /** The number of Telegram Stars that must be paid to send the sticker */
    @SerialName("star_count") val starCount: Int,

    /** The number of Telegram Stars that must be paid to upgrade the gift to a unique one */
    @SerialName("upgrade_star_count") val upgradeStarCount: Int? = null,

    /** The total number of the gifts of this type that can be sent; for limited gifts only */
    @SerialName("total_count") val totalCount: Int? = null,

    /** The number of remaining gifts of this type that can be sent; for limited gifts only */
    @SerialName("remaining_count") val remainingCount: Int? = null,
)

