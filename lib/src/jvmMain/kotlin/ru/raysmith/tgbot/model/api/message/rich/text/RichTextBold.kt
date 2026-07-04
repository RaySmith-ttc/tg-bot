package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A bold text. */
@Serializable
data class RichTextBold(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “bold” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "bold"
}
