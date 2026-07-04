package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Video
import ru.raysmith.tgbot.model.serializer.RichBlockVideoSerializer

/** A block with a video, corresponding to the HTML tag <video>. */
@Serializable
data class RichBlockVideo(

    /** The video(s) */
    @Serializable(RichBlockVideoSerializer::class)
    @SerialName("video") val video: List<Video>,

    /** *True*, if the media preview is covered by a spoiler animation */
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “video” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "video"
}
