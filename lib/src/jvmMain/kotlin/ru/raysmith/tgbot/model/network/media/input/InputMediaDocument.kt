package ru.raysmith.tgbot.model.network.media.input

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/**
 * Represents a general file to be sent.
 * */
@Serializable
data class InputMediaDocument(

    @SerialName("media") override val media: String,
    @SerialName("thumbnail") override val thumbnail: String? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /**
     * Disables automatic server-side content type detection for files uploaded using multipart/form-data.
     * Always True, if the document is sent as part of an album.
     * */
    @SerialName("disable_content_type_detection") val disableContentTypeDetection: Boolean? = null,
) : InputMediaGroupWithThumbnail() {

    /** Type of the result */
    @Required
    @SerialName("type") override val type: String = "document"
}