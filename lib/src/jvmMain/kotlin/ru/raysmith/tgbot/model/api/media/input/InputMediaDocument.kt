package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.KSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Transient
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

/**
 * Represents a general file to be sent.
 * */
@Serializable(InputMediaDocumentSerializer::class)
data class InputMediaDocument(

    @Transient override val media: InputFile,
    @Transient override val thumbnail: InputFile? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /**
     * Disables automatic server-side content type detection for files uploaded using multipart/form-data.
     * Always *True*, if the document is sent as part of an album.
     * */
    @SerialName("disable_content_type_detection") val disableContentTypeDetection: Boolean? = null,
) : InputWithMedia, InputWithThumbnail, InputPollMedia, InputMediaGroup, InputMedia, AttachmentsHolder {

    /** Type of the media, must be *document* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "document"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "thumbnail" to attachmentOf(thumbnail),
    ).also {
        println("IDS: ${it.map { it.value?.fieldName }}")
    }
}

internal object InputMediaDocumentSerializer : KSerializer<InputMediaDocument> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaDocument") {
        element<String>("type")
        element<String>("media")
        element<String?>("thumbnail", isOptional = true)
        element<String?>("caption")
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("disable_content_type_detection")
    }

    override fun serialize(encoder: Encoder, value: InputMediaDocument) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.thumbnail != null)  encodeStringElement(descriptor, 2, value.fieldValue("thumbnail"))
            if (value.caption != null) encodeStringElement(descriptor, 3, value.caption)
            if (value.parseMode != null) encodeNullableSerializableElement(
                descriptor, 4, ParseMode.serializer(), value.parseMode
            )
            if (value.captionEntities != null) encodeNullableSerializableElement(
                descriptor, 5, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.disableContentTypeDetection != null) encodeBooleanElement(
                descriptor, 6, value.disableContentTypeDetection
            )
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputMediaDocument can't be deserialized")
}
