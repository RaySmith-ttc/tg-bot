package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A text with a link. */
@Serializable
data class RichTextUrl(

    /** The text */
    @SerialName("text") val text: RichText,

    /** URL of the link */
    @SerialName("url") val url: String,
) : RichText {

    /** Type of the rich text, always “url” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "url"
}
