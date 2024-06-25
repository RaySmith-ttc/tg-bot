package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.chat.member.*

object ChatMemberSerializer : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatMember> {
        require(element is JsonObject)

        val statusElement = element["status"] ?: throw IllegalArgumentException("ChatMember response has no status field")
        require(statusElement is JsonPrimitive) { "ChatMember status field of is not primitive" }

        return when(val value = statusElement.jsonPrimitive.content) {
            "creator" -> ChatMemberOwner.serializer()
            "administrator" -> ChatMemberAdministrator.serializer()
            "member" -> ChatMemberMember.serializer()
            "restricted" -> ChatMemberRestricted.serializer()
            "left" -> ChatMemberLeft.serializer()
            "kicked" -> ChatMemberBanned.serializer()
            else -> throw IllegalArgumentException("ChatMember response has unknown status field: $value")
        }
    }
}