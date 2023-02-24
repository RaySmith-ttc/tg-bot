package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.network.media.input.InputFile

internal object FileIdOrUrlSerializer : KSerializer<InputFile.FileIdOrUrl> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FileIdOrUrl", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder) = InputFile.FileIdOrUrl(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: InputFile.FileIdOrUrl) {
        encoder.encodeString(value.value)
    }
}