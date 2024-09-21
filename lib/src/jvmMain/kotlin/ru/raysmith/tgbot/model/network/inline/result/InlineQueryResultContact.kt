package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/**
 * Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively,
 * you can use *[inputMessageContent]* to send a message with the specified content instead of the contact.
 * */
@Serializable
data class InlineQueryResultContact(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Contact's phone number */
    @SerialName("phone_number") val phoneNumber: String,

    /** Contact's first name */
    @SerialName("first_name") val firstName: String,

    /** Contact's last name */
    @SerialName("last_name") val lastName: String? = null,

    /** Additional data about the contact in the form of a vCard, 0-2048 bytes */
    @SerialName("vcard") val vcard: String? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the contact */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    /** Url of the thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Thumbnail width */
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    /** Thumbnail height */
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *contact* */
    @EncodeDefault
    override val type: String = "contact"
}