package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/**
 * Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user.
 * Alternatively, you can use *[inputMessageContent]* to send a message with the specified content instead of the
 * sticker.
 * */
@Serializable
data class InlineQueryResultCachedSticker(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid file identifier of the sticker */
    @SerialName("sticker_file_id") val stickerFileId: String,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the video animation */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    ) : InlineQueryResult() {

    /** Type of the result, must be *sticker* */
    @EncodeDefault
    override val type: String = "sticker"
}