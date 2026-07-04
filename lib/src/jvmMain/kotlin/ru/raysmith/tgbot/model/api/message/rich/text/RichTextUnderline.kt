package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** An underlined text. */
@Serializable
data class RichTextUnderline(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “underline” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "underline"
}
