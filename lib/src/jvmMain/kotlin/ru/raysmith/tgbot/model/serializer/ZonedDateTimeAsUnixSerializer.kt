package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

internal object ZonedDateTimeAsUnixSerializer : KSerializer<ZonedDateTime> {
    private val zoneId = ZoneId.of("Z")

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val seconds = decoder.decodeLong()
        return ZonedDateTime.from(Instant.ofEpochSecond(seconds).atZone(zoneId))
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeInt(value.toEpochSecond().toInt())

    }
}
