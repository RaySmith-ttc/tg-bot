package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.Animation
import ru.raysmith.tgbot.model.serializer.RichBlockAnimationSerializer

/** A block with an animation, corresponding to the HTML tag <video>. */
@Serializable
data class RichBlockAnimation(

    /** The animation(s) */
    @Serializable(RichBlockAnimationSerializer::class)
    @SerialName("animation") val animation: List<Animation>,

    /** *True*, if the media preview is covered by a spoiler animation */
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “animation” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "animation"
}
