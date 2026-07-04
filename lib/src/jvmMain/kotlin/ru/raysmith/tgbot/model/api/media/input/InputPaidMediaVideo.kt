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
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode
import ru.raysmith.tgbot.model.bot.AttachmentData
import ru.raysmith.tgbot.model.bot.AttachmentsHolder
import ru.raysmith.tgbot.model.bot.attachmentOf

/** The paid media to send is a video. */
@Serializable(InputPaidMediaVideoSerializer::class)
data class InputPaidMediaVideo(

    @SerialName("media") override val media: InputFile,
    @SerialName("thumbnail") override val thumbnail: InputFile? = null,

    /**
     * Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers
     * (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass
     * “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    @SerialName("cover") override val cover: InputFile? = null,

    /** Start timestamp for the video in the message */
    @SerialName("start_timestamp") val startTimestamp: Int? = null,

    /** Video width */
    @SerialName("width") val width: Int? = null,

    /** Video height */
    @SerialName("height") val height: Int? = null,

    /** Video duration */
    @SerialName("duration") val duration: Int? = null,

    /** Pass *True* if the uploaded video is suitable for streaming */
    @SerialName("supports_streaming") val supportsStreaming: Boolean? = null,
) : InputWithMedia, InputWithThumbnail, InputWithCover, InputPaidMedia, AttachmentsHolder {

    /** Type of the media, must be *video* */
    @EncodeDefault
    @SerialName("type") override val type: String = "video"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "thumbnail" to attachmentOf(thumbnail),
        "cover" to attachmentOf(cover),
    )
}

internal object InputPaidMediaVideoSerializer : KSerializer<InputPaidMediaVideo> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaVideo") {
        element<String>("type")
        element<String>("media")
        element<String?>("thumbnail", isOptional = true)
        element<String?>("cover", isOptional = true)
        element<Int?>("start_timestamp", isOptional = true)
        element<Int?>("width", isOptional = true)
        element<Int?>("height", isOptional = true)
        element<Int?>("duration", isOptional = true)
        element<Boolean?>("supports_streaming", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputPaidMediaVideo) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.thumbnail != null)  encodeStringElement(descriptor, 2, value.fieldValue("thumbnail"))
            if (value.cover != null) encodeStringElement(descriptor, 3, value.fieldValue("cover"))
            if (value.startTimestamp != null) encodeIntElement(descriptor, 4, value.startTimestamp)
            if (value.width != null) encodeIntElement(descriptor, 5, value.width)
            if (value.height != null) encodeIntElement(descriptor, 6, value.height)
            if (value.duration != null) encodeIntElement(descriptor, 7, value.duration)
            if (value.supportsStreaming != null) encodeBooleanElement(descriptor, 8, value.supportsStreaming)
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputPaidMediaVideo can't be deserialized")
}
