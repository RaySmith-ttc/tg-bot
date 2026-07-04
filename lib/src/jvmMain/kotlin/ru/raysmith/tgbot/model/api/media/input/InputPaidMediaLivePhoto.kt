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

/** The paid media to send is a live photo. */
@Serializable(InputPaidMediaLivePhotoSerializer::class)
data class InputPaidMediaLivePhoto(

    // TODO missed docs (should be override interface docs); other classes as well
    override val media: InputFile,

    /**
     * The static photo to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>”
     * to upload a new one using multipart/form-data under <file_attach_name> name.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    override val photo: InputFile,
) : InputWithMedia, InputWithPhoto, InputPaidMedia, AttachmentsHolder {

    /** Type of the media, must be *live_photo* */
    @EncodeDefault
    @SerialName("type") override val type: String = "live_photo"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "photo" to attachmentOf(photo),
    )
}

internal object InputPaidMediaLivePhotoSerializer : KSerializer<InputPaidMediaLivePhoto> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputPaidMediaLivePhoto") {
        element<String>("type")
        element<String>("media")
        element<String>("photo")
    }

    override fun serialize(encoder: Encoder, value: InputPaidMediaLivePhoto) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            encodeStringElement(descriptor, 2, value.fieldValue("photo"))
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputPaidMediaLivePhoto can't be deserialized")
}
