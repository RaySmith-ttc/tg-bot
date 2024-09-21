package ru.raysmith.tgbot.model.network.media.paid

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.Video

/** The paid media is a video. */
@Serializable
data class PaidMediaVideo(

    /** The video */
    val video: Video
) : PaidMedia() {

    /** Type of the paid media, always “video” */
    override val type: String = "video"
}