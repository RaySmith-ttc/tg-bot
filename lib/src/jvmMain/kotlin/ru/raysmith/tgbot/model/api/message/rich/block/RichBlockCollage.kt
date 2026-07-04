package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A collage, corresponding to the custom HTML tag <tg-collage>. */
@Serializable
data class RichBlockCollage(

    /** Elements of the collage */
    @SerialName("blocks") val blocks: List<RichBlock>,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “collage” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "collage"
}
