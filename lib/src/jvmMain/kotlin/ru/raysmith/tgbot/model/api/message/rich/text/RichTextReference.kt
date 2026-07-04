package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A reference. */
@Serializable
data class RichTextReference(

    /** Text of the reference */
    @SerialName("text") val text: RichText,

    /** The name of the reference */
    @SerialName("name") val name: String,
) : RichText {

    /** Type of the rich text, always “reference” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "reference"
}
