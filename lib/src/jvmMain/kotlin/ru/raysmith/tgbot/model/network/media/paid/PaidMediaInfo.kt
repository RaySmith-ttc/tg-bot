package ru.raysmith.tgbot.model.network.media.paid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.Media

/** Describes the paid media added to a message. */
@Serializable
data class PaidMediaInfo(

    /** The number of Telegram Stars that must be paid to buy access to the media */
    @SerialName("star_count") val starCount: Int,

    /** Information about the paid media */
    @SerialName("paid_media") val paidMedia: List<PaidMedia>,
) : Media

