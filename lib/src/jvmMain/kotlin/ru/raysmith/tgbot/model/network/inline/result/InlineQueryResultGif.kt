package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/** MIME type of the thumbnail */
@Serializable
enum class MimeTypeGif {
    @SerialName("image/jpeg") JPEG,
    @SerialName("image/gif") GIF,
    @SerialName("image/mp4") MP4;

    companion object {

        /** Returns supported [MimeTypeGif] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "image/jpeg" -> JPEG
            "image/gif" -> GIF
            "image/mp4" -> MP4
            else -> null
        }
    }
}

/**
 * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user
 * with optional caption. Alternatively, you can use *[inputMessageContent]* to send a message with the specified
 * content instead of the animation.
 * */
@Serializable
data class InlineQueryResultGif(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid URL for the GIF file */
    @SerialName("gif_url") val photoUrl: String,

    /** Width of the GIF */
    @SerialName("gif_width") val gifWidth: Int? = null,

    /** Height of the GIF */
    @SerialName("gif_height") val gifHeight: Int? = null,

    /** Duration of the GIF in seconds */
    @SerialName("gif_duration") val gifDuration: Int? = null,

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

    /** Pass *True*, if the caption must be shown above the message media */
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the GIF animation */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *gif* */
    @EncodeDefault
    override val type: String = "gif"
}