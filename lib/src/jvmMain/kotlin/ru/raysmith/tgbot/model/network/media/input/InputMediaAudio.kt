package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents an audio file to be treated as music to be sent.
 *
 * @property type Type of the result, must be *audio*
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
 * pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
 * to upload a new one using multipart/form-data under <file_attach_name> name.
 * @param caption Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption.
 *
 * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
 * */
@Serializable
data class InputMediaAudio(

    @SerialName("media") override val media: String,
    @SerialName("thumbnail") override val thumbnail: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Duration of the audio in seconds */
    @SerialName("duration") val duration: Int? = null,

    /** Performer of the audio */
    @SerialName("performer") val performer: String? = null,

    /** Title of the audio */
    @SerialName("title") val title: String? = null,
) : InputMediaGroupWithThumbnail() {

    /** Type of the result */
    @Required
    @SerialName("type") override val type: String = "audio"
}