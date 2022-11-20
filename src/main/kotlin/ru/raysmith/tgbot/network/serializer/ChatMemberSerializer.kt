package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.chat.ChatMember

object ChatMemberSerializer : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ChatMember> {
        require(element is JsonObject)

        val statusElement = element["status"] ?: throw IllegalArgumentException("ChatMember response has no status field")
        require(statusElement is JsonPrimitive) { "ChatMember status field of is not primitive" }

        return when(val value = statusElement.jsonPrimitive.content) {
            "creator" -> ChatMember.ChatMemberOwner.serializer()
            "administrator" -> ChatMember.ChatMemberAdministrator.serializer()
            "member" -> ChatMember.ChatMemberMember.serializer()
            "restricted" -> ChatMember.ChatMemberRestricted.serializer()
            "left" -> ChatMember.ChatMemberLeft.serializer()
            "kicked" -> ChatMember.ChatMemberBanned.serializer()
            else -> throw IllegalArgumentException("ChatMember response has unknown status field: $value")
        }
    }
}