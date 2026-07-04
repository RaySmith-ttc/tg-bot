package ru.raysmith.tgbot.model.api.sticker

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.InputMediaAnimation
import ru.raysmith.tgbot.model.api.message.MessageEntity
import ru.raysmith.tgbot.model.api.message.ParseMode
import ru.raysmith.tgbot.model.bot.AttachmentData
import ru.raysmith.tgbot.model.bot.AttachmentsHolder
import ru.raysmith.tgbot.model.bot.attachmentOf

// TODO docs

/** This object describes a sticker to be added to a sticker set. */
@Serializable(InputStickerSerializer::class)
data class InputSticker(
    val sticker: InputFile,
    val format: StickerFormat,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null
) : AttachmentsHolder {

    override val attachments: Map<String, AttachmentData?> = mapOf(
        "sticker" to attachmentOf(sticker),
    )
}

internal object InputStickerSerializer : KSerializer<InputSticker> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("InputSticker") {
        element<String>("sticker")
        element<StickerFormat>("format")
        element<List<String>>("emoji_list")
        element<MaskPosition?>("mask_position", isOptional = true)
        element<List<String>?>("keywords", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: InputSticker) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.fieldValue("sticker"))
            encodeSerializableElement(descriptor, 1, StickerFormat.serializer(), value.format)
            encodeSerializableElement(descriptor, 2, ListSerializer(String.serializer()), value.emojiList)
            if (value.maskPosition != null) {
                encodeSerializableElement(descriptor, 3, MaskPosition.serializer(), value.maskPosition)
            }
            if (value.keywords != null) {
                encodeSerializableElement(descriptor, 4, ListSerializer(String.serializer()), value.keywords)
            }
        }
    }

    override fun deserialize(decoder: Decoder) = throw SerializationException("InputSticker can't be deserialized")
}
