package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.message.LivePeriod

internal object LivePeriodSecondsSerializer : KSerializer<LivePeriod.Seconds> {
    override val descriptor = PrimitiveSerialDescriptor("LivePeriod.Seconds", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) = LivePeriod.Seconds(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: LivePeriod.Seconds) = encoder.encodeInt(value.value)
}
