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
@Serializable(InputMediaAudioSerializer::class)
data class InputMediaAudio(

    @SerialName("media") override val media: InputFile,
    @SerialName("thumbnail") override val thumbnail: InputFile? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Duration of the audio in seconds */
    @SerialName("duration") val duration: Int? = null,

    /** Performer of the audio */
    @SerialName("performer") val performer: String? = null,

    /** Title of the audio */
    @SerialName("title") val title: String? = null,
) : InputWithMedia, InputWithThumbnail, InputPollMedia, InputMediaGroup, InputMedia, AttachmentsHolder {

    /** Type of the media, must be *audio* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "audio"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "thumbnail" to attachmentOf(thumbnail),
    )
}

internal object InputMediaAudioSerializer : KSerializer<InputMediaAudio> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaAudio") {
        element<String>("type")
        element<String>("media")
        element<String?>("thumbnail", isOptional = true)
        element<String?>("caption")
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Int?>("duration")
        element<String?>("performer")
        element<String?>("title")
    }

    override fun serialize(encoder: Encoder, value: InputMediaAudio) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.thumbnail != null) encodeStringElement(descriptor, 2, value.fieldValue("thumbnail"))
            if (value.caption != null) encodeStringElement(descriptor, 3, value.caption)
            if (value.parseMode != null) encodeNullableSerializableElement(
                descriptor, 4, ParseMode.serializer(), value.parseMode
            )
            if (value.captionEntities != null) encodeNullableSerializableElement(
                descriptor, 5, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.duration != null) encodeIntElement(descriptor, 6, value.duration)
            if (value.performer != null) encodeStringElement(descriptor, 7, value.performer)
            if (value.title != null) encodeStringElement(descriptor, 8, value.title)
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputMediaAudio can't be deserialized")
}
