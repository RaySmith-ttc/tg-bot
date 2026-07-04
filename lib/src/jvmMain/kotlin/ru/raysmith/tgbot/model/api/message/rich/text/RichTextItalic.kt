package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An italicized text. */
@Serializable
data class RichTextItalic(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “italic” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "italic"
}
