package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A text with a phone number. */
@Serializable
data class RichTextPhoneNumber(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The phone number */
    @SerialName("phone_number") val phoneNumber: String,
) : RichText {

    /** Type of the rich text, always “phone_number” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "phone_number"
}
