package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.model.network.chat.ChatMember

object ChatMemberSerializer : KSerializer<ChatMember> {
    override fun deserialize(decoder: Decoder): ChatMember {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        require(element is JsonObject)

        val statusElement = element["status"] ?: throw IllegalArgumentException("ChatMember response has no status field")
        require(statusElement is JsonPrimitive) { "ChatMember status field of is not primitive" }

        return when(val value = statusElement.jsonPrimitive.content) {
            "creator" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberOwner.serializer(), element)
            "administrator" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberAdministrator.serializer(), element)
            "member" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberMember.serializer(), element)
            "restricted" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberRestricted.serializer(), element)
            "left" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberLeft.serializer(), element)
            "kicked" -> decoder.json.decodeFromJsonElement(ChatMember.ChatMemberBanned.serializer(), element)
            else -> throw IllegalArgumentException("ChatMember response has unknown status field: $value")
        }
    }

    override val descriptor: SerialDescriptor
        get() = throw NotImplementedError("ChatMember can't be serialized")

    override fun serialize(encoder: Encoder, value: ChatMember) {
        throw NotImplementedError("ChatMember can't be serialized")
    }

}