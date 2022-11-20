package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi

/**
 * Represents a general file to be sent.
 * */
@Serializable
data class InputMediaDocument(

    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
     * to upload a new one using multipart/form-data under <file_attach_name> name.
     *
     * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
     * */
    @SerialName("media") val media: String,

    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height
     * should not exceed 320. Ignored if the file is not uploaded using multipart/form-data.
     * Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>”
     * if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     *
     * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
     * */
    @SerialName("thumb") val thumb: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *parse_mode* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /**
     * Disables automatic server-side content type detection for files uploaded using multipart/form-data.
     * Always True, if the document is sent as part of an album.
     * */
    @SerialName("disable_content_type_detection") val disableContentTypeDetection: Boolean? = null,
) : InputMedia() {

    /** Type of the result */
    @Required
    @SerialName("type") override val type: String = "document"

    override fun toString(): String {
        return TelegramApi.json.encodeToString(this)
    }
}