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

/** Represents a video to be sent. */
@Serializable(InputMediaVideoSerializer::class)
data class InputMediaVideo(

    override val media: InputFile,
    override val thumbnail: InputFile? = null,

    /**
     * Cover for the video in the message. Pass a [InputFile.FileIdOrUrl] to send a file that exists on the Telegram
     * servers (recommended), pass an [InputFile.FileIdOrUrl] for Telegram to get a file from the Internet, or pass
     * [InputFile.File] or [InputFile.ByteArray] to upload a new one.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    override val cover: InputFile? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    val caption: String? = null,

    /** Mode for parsing entities in the photo caption. See [formatting options][ParseMode] for more details. */
    val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    val captionEntities: List<MessageEntity>? = null,

    /** Pass *True*, if the caption must be shown above the message media */
    val showCaptionAboveMedia: Boolean? = null,

    /** Video width */
    val width: Int? = null,

    /** Video height */
    val height: Int? = null,

    /** Video duration in seconds */
    val duration: Int? = null,

    /** Pass *True* if the uploaded video is suitable for streaming */
    val supportsStreaming: Boolean? = null,

    /** Pass *True* if the video needs to be covered with a spoiler animation */
    val hasSpoiler: Boolean? = null,
) : InputWithMedia, InputWithThumbnail, InputWithCover, InputMediaGroup, InputPollMedia, InputPollOptionMedia, InputMedia, AttachmentsHolder {

    /** Type of the media, must be *video* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "video"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "thumbnail" to attachmentOf(thumbnail),
        "cover" to attachmentOf(cover),
    )
}

internal object InputMediaVideoSerializer : KSerializer<InputMediaVideo> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaVideo") {
        element<String>("type")
        element<String>("media")
        element<String?>("thumbnail", isOptional = true)
        element<String?>("cover", isOptional = true)
        element<String?>("caption", isOptional = true)
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media", isOptional = true)
        element<Int?>("width", isOptional = true)
        element<Int?>("height", isOptional = true)
        element<Int?>("duration", isOptional = true)
        element<Boolean?>("supports_streaming", isOptional = true)
        element<Boolean?>("has_spoiler", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputMediaVideo) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.thumbnail != null)  encodeStringElement(descriptor, 2, value.fieldValue("thumbnail"))
            if (value.cover != null)  encodeStringElement(descriptor, 3, value.fieldValue("cover"))
            if (value.caption != null) encodeStringElement(descriptor, 4, value.caption)
            if (value.parseMode != null) encodeNullableSerializableElement(
                descriptor, 5, ParseMode.serializer(), value.parseMode
            )
            if (value.captionEntities != null) encodeNullableSerializableElement(
                descriptor, 6, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.showCaptionAboveMedia != null) encodeBooleanElement(descriptor, 7, value.showCaptionAboveMedia)
            if (value.width != null) encodeIntElement(descriptor, 8, value.width)
            if (value.height != null) encodeIntElement(descriptor, 9, value.height)
            if (value.duration != null) encodeIntElement(descriptor, 10, value.duration)
            if (value.supportsStreaming != null) encodeBooleanElement(descriptor, 11, value.supportsStreaming)
            if (value.hasSpoiler != null) encodeBooleanElement(descriptor, 12, value.hasSpoiler)
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputMediaVideo can't be deserialized")
}
