package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A text with an email address. */
@Serializable
data class RichTextEmailAddress(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The email address */
    @SerialName("email_address") val emailAddress: String,
) : RichText {

    /** Type of the rich text, always “email_address” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "email_address"
}
