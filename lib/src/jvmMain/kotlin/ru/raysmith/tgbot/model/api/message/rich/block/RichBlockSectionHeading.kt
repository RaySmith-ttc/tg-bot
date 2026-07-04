package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.rich.text.RichText

/** A section heading, corresponding to the HTML tags <h1>, <h2>, <h3>, <h4>, <h5>, or <h6>. */
@Serializable
data class RichBlockSectionHeading(

    /** Text of the block */
    @SerialName("text") val text: RichText,

    /** Relative size of the text font; 1-6, 1 is the largest, 6 is the smallest */
    @SerialName("size") val size: Int,
) : RichBlock {

    /** Type of the block, always “heading” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "heading"
}
