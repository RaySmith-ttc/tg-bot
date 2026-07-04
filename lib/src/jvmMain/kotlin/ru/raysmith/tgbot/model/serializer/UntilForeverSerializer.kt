package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.Until

internal object UntilForeverSerializer : KSerializer<Until.Forever> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Until.Forever", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Until.Forever) = encoder.encodeInt(0)

    override fun deserialize(decoder: Decoder) = error("Until.Forever can't be deserialized")
}
