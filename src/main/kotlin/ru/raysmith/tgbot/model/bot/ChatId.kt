package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

//// TODO use instead strings
//sealed class ChatId {
//    data class ID(val value: Long) : ChatId()
//
//    class ChannelUsername(username: String) : ChatId() {
//        val username: String = if (username.startsWith("@")) username else "@$username"
//    }
//
//    companion object {
//        fun fromId(id: Long) = ID(id)
//        fun fromChannelUsername(username: String) = ChannelUsername(username)
//    }
//}

sealed class ChatId {

    companion object {
        fun fromId(id: Long) = ID(id)
        fun fromUsername(username: String) = Username(username)
    }

    @Serializable(with = IDSerializer::class)
    data class ID(val value: Long) : ChatId()

    @Serializable(with = UsernameSerializer::class)
    data class Username(private val value: String) : ChatId() {
        val username: String = if (value.startsWith("@")) value else "@$value"
    }

    object IDSerializer : KSerializer<ID> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.ID", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): ID = ID(decoder.decodeLong())
        override fun serialize(encoder: Encoder, value: ID) {
            encoder.encodeLong(value.value)
        }
    }

    object UsernameSerializer : KSerializer<Username> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.Username", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Username = Username(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: Username) {
            encoder.encodeString(value.username)
        }
    }
}