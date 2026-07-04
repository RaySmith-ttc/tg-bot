package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextPlain

/** Serializes/deserializes a [RichTextPlain] as a raw JSON string. */
internal object RichTextPlainSerializer : KSerializer<RichTextPlain> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RichTextPlain", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: RichTextPlain) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): RichTextPlain {
        return RichTextPlain(decoder.decodeString())
    }
}
