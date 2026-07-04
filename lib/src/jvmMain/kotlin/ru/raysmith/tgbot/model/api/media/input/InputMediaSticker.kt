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

/** Represents a sticker file to be sent. */
@Serializable(InputMediaStickerSerializer::class)
data class InputMediaSticker(

    @SerialName("media") override val media: InputFile,

    /** Emoji associated with the sticker; only for just uploaded stickers */
    @SerialName("emoji") val emoji: String? = null,
) : InputPollOptionMedia, InputWithMedia, AttachmentsHolder {

    /** Type of the media, must be *sticker* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "sticker"

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "media" to attachmentOf(media),
    )
}

internal object InputMediaStickerSerializer : KSerializer<InputMediaSticker> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputMediaSticker") {
        element<String>("type")
        element<String>("media")
        element<String?>("emoji")
    }

    override fun serialize(encoder: Encoder, value: InputMediaSticker) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeStringElement(descriptor, 1, value.fieldValue("media"))
            if (value.emoji != null)  encodeStringElement(descriptor, 2, value.emoji)
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputMediaSticker can't be deserialized")
}
