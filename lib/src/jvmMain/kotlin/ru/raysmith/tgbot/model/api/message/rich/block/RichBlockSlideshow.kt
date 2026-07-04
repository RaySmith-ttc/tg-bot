package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A slideshow, corresponding to the custom HTML tag <tg-slideshow>. */
@Serializable
data class RichBlockSlideshow(

    /** Elements of the slideshow */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “slideshow” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "slideshow"
}
