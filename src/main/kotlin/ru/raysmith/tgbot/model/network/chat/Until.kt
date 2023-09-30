package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime

@Serializable(with = UntilSerializer::class)
sealed class Until {
    data object Forever : Until()
    data class Date(val date: ZonedDateTime) : Until()
}

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