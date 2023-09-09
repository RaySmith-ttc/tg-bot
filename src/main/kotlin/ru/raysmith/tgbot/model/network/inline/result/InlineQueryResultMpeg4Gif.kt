package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound).
 * By default, this animated MPEG-4 file will be sent by the user with optional caption.
 * Alternatively, you can use *[inputMessageContent]* to send a message with the specified content instead of the
 * animation.
 * */
@Serializable
data class InlineQueryResultMpeg4Gif(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid URL for the MPEG4 file. File size must not exceed 1MB */
    @SerialName("mpeg4_url") val mpeg4Url: String,

    /** Video width */
    @SerialName("mpeg4_width") val mpeg4Width: Int? = null,

    /** Video height */
    @SerialName("mpeg4_height") val mpeg4Height: Int? = null,

    /** Video duration in seconds */
    @SerialName("mpeg4_duration") val mpeg4Duration: Int? = null,

    /** URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** MIME type of the thumbnail. Defaults to [MimeTypeGif.JPEG] */
    @SerialName("thumbnail_mime_type") val thumbnailMimeType: MimeTypeGif? = null,

    /** Title for the result */
    @SerialName("title") val title: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the message text. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the video animation */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    ) : InlineQueryResult() {

    /** Type of the result, must be *mpeg4_gif* */
    @EncodeDefault
    override val type: String = "mpeg4_gif"
}