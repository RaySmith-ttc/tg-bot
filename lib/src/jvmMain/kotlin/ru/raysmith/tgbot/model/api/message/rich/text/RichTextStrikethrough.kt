package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A strikethrough text. */
@Serializable
data class RichTextStrikethrough(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “strikethrough” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "strikethrough"
}
