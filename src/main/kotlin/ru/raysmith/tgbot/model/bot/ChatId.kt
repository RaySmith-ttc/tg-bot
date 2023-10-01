package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.network.serializer.ChatIdSerializer

// TODO docs
@Polymorphic
@Serializable(with = ChatIdSerializer::class)
sealed class ChatId {

    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is ChatId) false
        else toStringValue() == other.toStringValue()
    }

    override fun hashCode(): Int {
        return toStringValue().hashCode()
    }

    fun toRequestBody(): RequestBody = when (this) {
        is ID -> value.toString().toRequestBody()
        is Username -> value.toRequestBody()
    }

    fun toStringValue() = when (this) {
        is ID -> value.toString()
        is Username -> value
    }

    companion object {
        fun of(value: Any): ChatId {
            return when (value) {
                is Number -> ID(value.toLong())
                is String -> Username(value)
                else -> throw IllegalArgumentException("ChatId can only be created from Long or String")
            }
        }
    }

    @Serializable(with = IDSerializer::class)
    data class ID(val value: Long) : ChatId()

    /** Represents username of the user or supergroup (in any format `@username` or `username`)*/
    @Serializable(with = UsernameSerializer::class)
    data class Username(private val _value: String) : ChatId() {
        val value = _value.replaceFirstChar { if (it == '@') "@" else "@$it" }
    }

    internal object IDSerializer : KSerializer<ID> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.ID", PrimitiveKind.LONG)
        override fun deserialize(decoder: Decoder): ID = ID(decoder.decodeLong())
        override fun serialize(encoder: Encoder, value: ID) {
            encoder.encodeLong(value.value)
        }
    }

    internal object UsernameSerializer : KSerializer<Username> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.Username", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Username = Username(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: Username) {
            encoder.encodeString(value.value)
        }
    }
}

fun Any.asChatId() = ChatId.of(this)