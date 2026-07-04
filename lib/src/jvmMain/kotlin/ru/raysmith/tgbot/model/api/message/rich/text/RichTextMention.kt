package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A mention by a username. */
@Serializable
data class RichTextMention(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The username */
    @SerialName("username") val username: String,
) : RichText {

    /** Type of the rich text, always “mention” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "mention"
}
