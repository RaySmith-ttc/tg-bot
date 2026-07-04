package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A subscript text. */
@Serializable
data class RichTextSubscript(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “subscript” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "subscript"
}
