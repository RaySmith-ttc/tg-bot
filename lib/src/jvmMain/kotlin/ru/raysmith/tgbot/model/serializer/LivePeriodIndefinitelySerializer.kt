package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.message.LivePeriod

internal object LivePeriodIndefinitelySerializer : KSerializer<LivePeriod.Indefinitely> {
    override val descriptor = PrimitiveSerialDescriptor("LivePeriod", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: LivePeriod.Indefinitely) = encoder.encodeInt(0x7FFFFFFF)

    override fun deserialize(decoder: Decoder) = LivePeriod.Indefinitely
}
