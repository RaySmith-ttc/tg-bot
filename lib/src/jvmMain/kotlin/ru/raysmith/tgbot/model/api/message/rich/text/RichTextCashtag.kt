package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A cashtag. */
@Serializable
data class RichTextCashtag(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The cashtag */
    @SerialName("cashtag") val cashtag: String,
) : RichText {

    /** Type of the rich text, always “cashtag” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "cashtag"
}
