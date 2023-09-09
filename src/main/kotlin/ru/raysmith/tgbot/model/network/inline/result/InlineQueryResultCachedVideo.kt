package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a link to a video file stored on the Telegram servers. By default, this video file will be sent by the
 * user with an optional caption. Alternatively, you can use *[inputMessageContent]* to send a message with the
 * specified content instead of the video.
 *
 * > If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you **must** replace its content
 * using *[inputMessageContent]*.
 * */
@Serializable
data class InlineQueryResultCachedVideo(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid file identifier for the video file */
    @SerialName("video_file_id") val videoFileId: String,

    /** Title for the result */
    @SerialName("title") val title: String? = null,

    /** Short description of the result */
    @SerialName("description") val description: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the video caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

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