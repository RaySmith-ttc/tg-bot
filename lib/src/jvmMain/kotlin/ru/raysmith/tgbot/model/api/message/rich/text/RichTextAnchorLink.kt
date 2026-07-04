package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A link to an anchor. */
@Serializable
data class RichTextAnchorLink(

    /** The link text */
    @SerialName("text") val text: RichText,

    /** The name of the anchor. If the name is empty, then the link brings back to the top of the message. */
    @SerialName("anchor_name") val anchorName: String,
) : RichText {

    /** Type of the rich text, always “anchor_link” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "anchor_link"
}
