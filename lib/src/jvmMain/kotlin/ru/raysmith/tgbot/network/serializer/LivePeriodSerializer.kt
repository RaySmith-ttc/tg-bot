package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.message.LivePeriod
import kotlin.time.Duration.Companion.seconds

object LivePeriodSerializer : KSerializer<LivePeriod> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LivePeriod", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): LivePeriod {
        return when(val value = decoder.decodeInt()) {
            0x7FFFFFFF -> LivePeriod.Indefinitely
            else -> LivePeriod.Duration(value.seconds)
        }
    }

    override fun serialize(encoder: Encoder, value: LivePeriod) {
        when(value) {
            is LivePeriod.Seconds -> encoder.encodeInt(value.value)
            is LivePeriod.Duration -> encoder.encodeInt(value.value.inWholeSeconds.toInt())
            is LivePeriod.Indefinitely -> encoder.encodeInt(0x7FFFFFFF)
        }
    }
}