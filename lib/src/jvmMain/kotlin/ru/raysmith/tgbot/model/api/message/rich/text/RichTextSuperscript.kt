package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A superscript text. */
@Serializable
data class RichTextSuperscript(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “superscript” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "superscript"
}
