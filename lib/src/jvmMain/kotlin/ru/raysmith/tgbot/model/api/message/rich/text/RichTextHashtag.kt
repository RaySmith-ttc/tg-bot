package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A hashtag. */
@Serializable
data class RichTextHashtag(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The hashtag */
    @SerialName("hashtag") val hashtag: String,
) : RichText {

    /** Type of the rich text, always “hashtag” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "hashtag"
}
