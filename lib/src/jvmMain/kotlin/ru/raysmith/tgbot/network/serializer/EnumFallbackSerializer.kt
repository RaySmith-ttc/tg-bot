package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.network.NetworkUtils.getSerialName
import kotlin.enums.EnumEntries

internal open class EnumFallbackSerializer<E>(
    serialName: String,
    private val entries: EnumEntries<E>, private val fallback: E, kind: PrimitiveKind = PrimitiveKind.STRING
) : KSerializer<E> where  E : Enum<E> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, kind)

    override fun serialize(encoder: Encoder, value: E) {
        encoder.encodeString(value.getSerialName())
    }

    override fun deserialize(decoder: Decoder): E =
        decoder.decodeString().let { value ->
            entries.find { it.getSerialName() == value } ?: fallback
        }
}