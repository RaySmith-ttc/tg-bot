package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.Until

internal object UntilDateSerializer : KSerializer<Until.Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Until.Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Until.Date) =
        encoder.encodeInt(value.date.toEpochSecond().toInt())

    override fun deserialize(decoder: Decoder) = error("Until.Date can't be deserialized")
}
