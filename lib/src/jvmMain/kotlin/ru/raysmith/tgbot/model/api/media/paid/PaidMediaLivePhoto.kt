package ru.raysmith.tgbot.model.api.media.paid

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.LivePhoto

/** The paid media is a live photo. */
@Serializable
data class PaidMediaLivePhoto(

    /** The photo */
    @SerialName("live_photo") val livePhoto: LivePhoto,
) : PaidMedia {

    /** Type of the paid media, always “live_photo” */
    @EncodeDefault
    @SerialName("type") override val type: String = "live_photo"
}
