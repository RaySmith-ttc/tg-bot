package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An anchor. */
@Serializable
data class RichTextAnchor(

    /** The name of the anchor */
    @SerialName("name") val name: String,
) : RichText {

    /** Type of the rich text, always “anchor” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "anchor"
}
