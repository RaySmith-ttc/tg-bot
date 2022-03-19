package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.network.NetworkUtils.getSerialName
import kotlin.reflect.KClass

open class EnumFallbackSerializer<E>(
    private val kClass: KClass<E>, private val fallback: E
) : KSerializer<E> where  E : Enum<E> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("EnumSerialNameSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: E) {
        encoder.encodeString(value.getSerialName())
    }

    override fun deserialize(decoder: Decoder): E =
        decoder.decodeString().let { value ->
            kClass.java.enumConstants.firstOrNull { it.getSerialName() == value } ?: fallback
        }
}