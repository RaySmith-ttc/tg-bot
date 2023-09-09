package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a link to a photo. By default, this photo will be sent by the user with optional caption.
 * Alternatively, you can use [inputMessageContent] to send a message with the specified content instead of the photo.
 * */
@Serializable
data class InlineQueryResultPhoto(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid URL of the photo. Photo must be in **JPEG** format. Photo size must not exceed 5MB */
    @SerialName("photo_url") val photoUrl: String,

    /** Url of the thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Width of the photo */
    @SerialName("photo_width") val photoWidth: Int? = null,

    /** Height of the photo */
    @SerialName("photo_height") val photoHeight: Int? = null,

    /** Title for the result */
    @SerialName("title") val title: String? = null,

    /** Short description of the result */
    @SerialName("description") val description: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the message text. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the photo */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *photo* */
    @EncodeDefault
    override val type: String = "photo"
}

