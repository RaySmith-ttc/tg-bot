package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.network.chat.Until

object UntilSerializer : KSerializer<Until> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Until", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = error("Until can't be deserialized")

    override fun serialize(encoder: Encoder, value: Until) {
        when(value) {
            is Until.Forever -> encoder.encodeInt(0)
            is Until.Date -> encoder.encodeInt(value.date.toEpochSecond().toInt())
        }
    }
}