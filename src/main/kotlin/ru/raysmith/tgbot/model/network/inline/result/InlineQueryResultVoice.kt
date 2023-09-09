package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording
 * will be sent by the user. Alternatively, you can use *[inputMessageContent]* to send a message with the specified
 * content instead of the voice message.
 * */
@Serializable
data class InlineQueryResultVoice(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** A valid URL for the voice recording */
    @SerialName("voice_url") val voiceUrl: String,

    /** Recording title */
    @SerialName("title") val title: String? = null,

    /** Caption, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the voice message caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Recording duration in seconds */
    @SerialName("voice_duration") val voiceDuration: Int? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the voice recording */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *voice* */
    @EncodeDefault
    override val type: String = "voice"
}