package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.message.reaction.ReactionType
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeCustomEmoji
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeEmoji
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypePaid

object ReactionTypeSerializer : JsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReactionType> {
        val typeObject = element.jsonObject["type"] ?: error("The ReactionType json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The ReactionType json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The ReactionType json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "emoji" -> ReactionTypeEmoji.serializer()
            "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
            "paid" -> ReactionTypePaid.serializer()
            else -> error("Unknown ReactionType type: '$type'")
        }
    }
}