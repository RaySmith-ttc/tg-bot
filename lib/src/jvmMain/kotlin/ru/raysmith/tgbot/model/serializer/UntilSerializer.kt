package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.bot.Until

internal object UntilSerializer : KSerializer<Until> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Until", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Until) {
        when (value) {
            is Until.Forever -> Until.Forever.serializer().serialize(encoder, value)
            is Until.Date -> Until.Date.serializer().serialize(encoder, value)
        }
    }

    override fun deserialize(decoder: Decoder) = error("Until can't be deserialized")
}
