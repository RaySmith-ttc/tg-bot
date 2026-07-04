package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A link to a reference. */
@Serializable
data class RichTextReferenceLink(

    /** The link text */
    @SerialName("text") val text: RichText,

    /** The name of the reference */
    @SerialName("reference_name") val referenceName: String,
) : RichText {

    /** Type of the rich text, always “reference_link” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "reference_link"
}
