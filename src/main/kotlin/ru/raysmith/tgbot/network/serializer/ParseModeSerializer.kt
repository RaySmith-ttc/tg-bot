package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.network.message.ParseMode

object ParseModeSerializer : KSerializer<ParseMode> {
    override val descriptor = PrimitiveSerialDescriptor("ParseMode", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = ParseMode.valueOf(decoder.decodeString().uppercase())

    override fun serialize(encoder: Encoder, value: ParseMode) {
        encoder.encodeString(value.name.lowercase())
    }
}