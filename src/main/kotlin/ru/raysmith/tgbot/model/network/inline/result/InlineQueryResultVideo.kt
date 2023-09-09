package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/** MIME type of the video */
@Serializable
enum class MimeTypeVideo {
    @SerialName("text/html") HTML,
    @SerialName("video/mp4") MP4;

    companion object {

        /** Returns supported [MimeTypeVideo] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "text/html" -> HTML
            "video/mp4" -> MP4
            else -> null
        }
    }
}

/**
 * Represents a link to a page containing an embedded video player or a video file. By default, this video file will
 * be sent by the user with an optional caption. Alternatively, you can use *[inputMessageContent]* to send
 * a message with the specified content instead of the video.
 *
 * > If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you **must** replace its content
 * using *[inputMessageContent]*.
 * */
@Serializable
data class InlineQueryResultVideo(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid URL for the embedded video player or video file */
    @SerialName("video_url") val videoUrl: String,

    /** MIME type of the content of the video URL */
    @SerialName("mime_type") val mimeType: MimeTypeVideo? = null,

    /** URL of the thumbnail (JPEG only) for the video */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Title for the result */
    @SerialName("title") val title: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the video caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Video width */
    @SerialName("video_width") val videoWidth: Int? = null,

    /** Video height */
    @SerialName("video_height") val videoHeight: Int? = null,

    /** Video duration in seconds */
    @SerialName("video_duration") val videoDuration: Int? = null,

    /** Short description of the result */
    @SerialName("description") val description: String? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /**
     * Content of the message to be sent instead of the video. This field is **required** if [InlineQueryResultVideo]
     * is used to send an HTML-page as a result (e.g., a YouTube video).
     * */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *video* */
    @EncodeDefault
    override val type: String = "video"
}

