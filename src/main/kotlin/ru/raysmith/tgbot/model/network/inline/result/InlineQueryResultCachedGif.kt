package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will
 * be sent by the user with an optional caption. Alternatively, you can use *[inputMessageContent]* to send a
 * message with the specified content instead of the animation.
 * */
@Serializable
data class InlineQueryResultCachedGif(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid file identifier for the GIF file */
    @SerialName("gif_file_id") val gifFileId: String,

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