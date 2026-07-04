package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A text covered by a spoiler. */
@Serializable
data class RichTextSpoiler(

    /** The text */
    @SerialName("text") val text: RichText,
) : RichText {

    /** Type of the rich text, always “spoiler” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "spoiler"
}
