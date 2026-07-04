package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode
import ru.raysmith.tgbot.model.bot.AttachmentData
import ru.raysmith.tgbot.model.bot.AttachmentsHolder
import ru.raysmith.tgbot.model.bot.attachmentOf

// TODO replace all file_id with InputFile links just like in API (e.g. sendPhoto)
/**
 * Represents a photo to be sent.
 *
 * @property type Type of the result, must be *photo*
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
 *  pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
 *  to upload a new one using multipart/form-data under <file_attach_name> name.
 * @param caption Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption.
 * @param captionEntities List of special entities that appear in the caption, which can be specified instead of
 *  *[parseMode]*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
 * @param hasSpoiler Pass *True* if the photo needs to be covered with a spoiler animation
 *
 * @see <a href="https://core.telegram.org/bots/api#sending-files">More info on Sending Files »</a>
 * */
@Serializable(InputMediaPhotoSerializer::class)
data class InputMediaPhoto(
    @SerialName("media") override val media: InputFile,
    @SerialName("caption") val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
) : InputMediaGroup, InputPollMedia, InputPollOptionMedia, InputWithMedia, InputMedia, AttachmentsHolder {

    /** Type of the result */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
    )
}

internal object InputMediaPhotoSerializer : KSerializer<InputMediaPhoto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaPhoto") {
        element<String>("type")
        element<String>("media")
        element<String?>("caption")
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media")
        element<Boolean?>("has_spoiler")
    }

    override fun serialize(encoder: Encoder, value: InputMediaPhoto) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.caption != null) encodeStringElement(descriptor, 2, value.caption)
            if (value.parseMode != null) encodeNullableSerializableElement(
                descriptor, 3, ParseMode.serializer(), value.parseMode,
            )
            if (value.captionEntities != null) encodeNullableSerializableElement(
                descriptor, 4, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.showCaptionAboveMedia != null) encodeBooleanElement(descriptor, 5, value.showCaptionAboveMedia)
            if (value.hasSpoiler != null) encodeBooleanElement(descriptor, 6, value.hasSpoiler)
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputMediaPhoto can't be deserialized")
}
