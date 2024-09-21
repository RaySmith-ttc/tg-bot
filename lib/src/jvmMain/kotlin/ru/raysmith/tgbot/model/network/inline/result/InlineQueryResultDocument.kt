package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/** MIME type of the content of the file */
@Serializable
enum class MimeTypeDocument {
    @SerialName("application/pdf") PDF,
    @SerialName("application/zip") ZIP;

    companion object {

        /** Returns supported [MimeTypeDocument] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "application/pdf" -> PDF
            "application/zip" -> ZIP
            else -> null
        }
    }
}

/**
 * Represents a link to a file. By default, this file will be sent by the user with an optional caption.
 * Alternatively, you can use *[inputMessageContent]* to send a message with the specified content instead of the file.
 * Currently, only **.PDF** and **.ZIP** files can be sent using this method.
 * */
@Serializable
data class InlineQueryResultDocument(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Title for the result */
    @SerialName("title") val title: String? = null,

    /** A valid URL for the file */
    @SerialName("document_url") val documentUrl: String,

    /** MIME type of the content of the file */
    @SerialName("mime_type") val mimeType: MimeTypeDocument,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the message text. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Short description of the result */
    @SerialName("description") val description: String? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the file*/
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    /** URL of the thumbnail (JPEG only) for the file */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Thumbnail width */
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    /** Thumbnail height */
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *document* */
    @EncodeDefault
    override val type: String = "document"
}