package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.KSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
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

/** Represents a live photo to be sent. */
@Serializable(InputMediaLivePhotoSerializer::class)
data class InputMediaLivePhoto(

    @SerialName("media") override val media: InputFile,

    /**
     * The static photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended) or pass
     * “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name.
     * [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files).
     * Sending live photos by a URL is currently unsupported.
     * */
    @SerialName("photo") val photo: InputFile,

    /** Caption of the live photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the live photo caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of [parseMode] */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Pass *True* , if the caption must be shown above the message media */
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,

    /** Pass *True* if the live photo needs to be covered with a spoiler animation */
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
) : InputMediaGroup, InputPollMedia, InputPollOptionMedia, InputWithMedia, InputMedia, AttachmentsHolder {

    /** Type of the media, must be *live_photo* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "live_photo"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "photo" to attachmentOf(photo),
    )
}

internal object InputMediaLivePhotoSerializer : KSerializer<InputMediaLivePhoto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaLivePhoto") {
        element<String>("type")
        element<String>("media")
        element<String>("photo")
        element<String?>("caption")
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media")
        element<Boolean?>("has_spoiler")
    }

    override fun serialize(encoder: Encoder, value: InputMediaLivePhoto) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            encodeStringElement(descriptor, 2, value.fieldValue("photo"))
            if (value.caption != null) encodeStringElement(descriptor, 3, value.caption)
            if (value.parseMode != null) encodeNullableSerializableElement(
                descriptor, 4, ParseMode.serializer(), value.parseMode,
            )
            if (value.captionEntities != null) encodeNullableSerializableElement(
                descriptor, 5, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.showCaptionAboveMedia != null) encodeBooleanElement(descriptor, 6, value.showCaptionAboveMedia)
            if (value.hasSpoiler != null) encodeBooleanElement(descriptor, 7, value.hasSpoiler)
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputMediaLivePhoto can't be deserialized")
}
