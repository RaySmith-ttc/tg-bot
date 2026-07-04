package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.message.LivePeriod
import kotlin.time.Duration.Companion.seconds

internal object LivePeriodDurationSerializer : KSerializer<LivePeriod.Duration> {
    override val descriptor = PrimitiveSerialDescriptor("LivePeriod.Duration", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) = LivePeriod.Duration(decoder.decodeInt().seconds)

    override fun serialize(encoder: Encoder, value: LivePeriod.Duration) = encoder.encodeInt(value.value.inWholeSeconds.toInt())
}
