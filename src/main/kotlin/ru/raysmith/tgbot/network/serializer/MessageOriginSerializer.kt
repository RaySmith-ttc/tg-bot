package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.message.origin.*

object MessageOriginSerializer : JsonContentPolymorphicSerializer<MessageOrigin>(MessageOrigin::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MessageOrigin> {
        val typeObject = element.jsonObject["type"] ?: error("The MessageOrigin json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The MessageOrigin json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The MessageOrigin json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "user" -> MessageOriginUser.serializer()
            "hidden_user" -> MessageOriginHiddenUser.serializer()
            "chat" -> MessageOriginChat.serializer()
            "channel" -> MessageOriginChannel.serializer()
            else -> error("Unknown MessageOrigin type: '$type'")
        }
    }
}

