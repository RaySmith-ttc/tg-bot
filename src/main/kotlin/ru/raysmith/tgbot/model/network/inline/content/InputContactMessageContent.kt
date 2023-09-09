package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [content][InputMessageContent] of a contact message to be sent as the result of an inline query. */
@Serializable
data class InputContactMessageContent(

    /** Contact's phone number */
    @SerialName("phone_number") val phoneNumber: String,

    /** Contact's first name */
    @SerialName("first_name") val firstName: String,

    /** Contact's last name */
    @SerialName("last_name") val lastName: String? = null,

    /** Additional data about the contact in the form of a vCard, 0-2048 bytes */
    @SerialName("vcard") val vcard: String? = null,
) : InputMessageContent()