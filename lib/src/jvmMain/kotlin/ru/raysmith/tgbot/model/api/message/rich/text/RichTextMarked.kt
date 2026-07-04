package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A marked text. */
@Serializable
data class RichTextMarked(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “marked” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "marked"
}
