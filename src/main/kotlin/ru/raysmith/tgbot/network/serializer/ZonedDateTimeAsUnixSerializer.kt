package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime

internal object ZonedDateTimeAsUnixSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder) = error("ZonedDateTime can't be deserialized with ZonedDateTimeAsUnixSerializer")
    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeInt(value.toEpochSecond().toInt())
    }
}