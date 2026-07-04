package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.KSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.bot.AttachmentData
import ru.raysmith.tgbot.model.bot.AttachmentsHolder
import ru.raysmith.tgbot.model.bot.attachmentOf

/** The paid media to send is a photo. */
@Serializable(InputPaidMediaPhotoSerializer::class)
data class InputPaidMediaPhoto(

    /** File to send */
    override val media: InputFile,
) : InputPaidMedia, AttachmentsHolder {

    /** Type of the media, must be *photo* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
    )
}

internal object InputPaidMediaPhotoSerializer : KSerializer<InputPaidMediaPhoto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputPaidMediaPhoto") {
        element<String>("type")
        element<String>("media")
    }

    override fun serialize(encoder: Encoder, value: InputPaidMediaPhoto) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputPaidMediaPhoto can't be deserialized")
}
