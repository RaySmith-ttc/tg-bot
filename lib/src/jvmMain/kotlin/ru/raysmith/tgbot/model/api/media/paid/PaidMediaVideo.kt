package ru.raysmith.tgbot.model.api.media.paid

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Video

/** The paid media is a video. */
@Serializable
data class PaidMediaVideo(

    /** The video */
    val video: Video
) : PaidMedia {

    /** Type of the paid media, always “video” */
    @EncodeDefault
    @SerialName("type") override val type: String = "video"
}
