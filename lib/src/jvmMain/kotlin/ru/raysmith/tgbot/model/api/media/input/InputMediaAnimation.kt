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

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC Animation without sound) to be sent.
 * */
@Serializable(InputMediaAnimationSerializer::class)
data class InputMediaAnimation(

    @Transient override val media: InputFile,
    @Transient override val thumbnail: InputFile? = null,

    /** Caption of the photo to be sent, 0-1024 characters after entities parsing */
    @SerialName("caption") val caption: String? = null,

    /** Mode for parsing entities in the photo caption. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in the caption, which can be specified instead of *[parseMode]* */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Pass *True*, if the caption must be shown above the message media */
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,

    /** Animation width */
    @SerialName("width") val width: Int? = null,

    /** Animation height */
    @SerialName("height") val height: Int? = null,

    /** Animation duration in seconds */
    @SerialName("duration") val duration: Int? = null,

    /** Pass *True* if the animation needs to be covered with a spoiler animation */
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,
) : InputWithMedia, InputWithThumbnail, InputPollMedia, InputPollOptionMedia, InputMediaGroup, InputMedia,
    AttachmentsHolder {

    /** Type of the media, must be *animation* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "animation"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
        "thumbnail" to attachmentOf(thumbnail),
    )
}

internal object InputMediaAnimationSerializer : KSerializer<InputMediaAnimation> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaAnimation") {
        element<String>("type")
        element<String>("media")
        element<String?>("thumbnail", isOptional = true)
        element<String?>("caption", isOptional = true)
        element<ParseMode?>("parse_mode", isOptional = true)
        element<List<MessageEntity>?>("caption_entities", isOptional = true)
        element<Boolean?>("show_caption_above_media", isOptional = true)
        element<Int?>("width", isOptional = true)
        element<Int?>("height", isOptional = true)
        element<Int?>("duration", isOptional = true)
        element<Boolean?>("has_spoiler", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputMediaAnimation) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.thumbnail != null) encodeStringElement(descriptor, 2, value.fieldValue("thumbnail"))
            if (value.caption != null) encodeStringElement(descriptor, 3, value.caption)
            if (value.parseMode != null) encodeSerializableElement(
                descriptor, 4, ParseMode.serializer(), value.parseMode
            )
            if (value.captionEntities != null) encodeSerializableElement(
                descriptor, 5, ListSerializer(MessageEntity.serializer()), value.captionEntities
            )
            if (value.showCaptionAboveMedia != null) encodeBooleanElement(descriptor, 6, value.showCaptionAboveMedia)
            if (value.width != null) encodeIntElement(descriptor, 7, value.width)
            if (value.height != null) encodeIntElement(descriptor, 8, value.height)
            if (value.duration != null) encodeIntElement(descriptor, 9, value.duration)
            if (value.hasSpoiler != null) encodeBooleanElement(descriptor, 10, value.hasSpoiler)
        }
    }

    override fun deserialize(decoder: Decoder) =
        throw SerializationException("InputMediaAnimation can't be deserialized")
}
