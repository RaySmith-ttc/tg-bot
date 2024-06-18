package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostSource
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostSourceGiftCode
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostSourceGiveaway
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostSourcePremium

object ChatBoostSourceSerializer : JsonContentPolymorphicSerializer<ChatBoostSource>(ChatBoostSource::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatBoostSource> {
        val typeObject = element.jsonObject["type"] ?: error("The ChatBoostSource json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The ChatBoostSource json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The ChatBoostSource json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "premium" -> ChatBoostSourcePremium.serializer()
            "gift_code" -> ChatBoostSourceGiftCode.serializer()
            "giveaway" -> ChatBoostSourceGiveaway.serializer()
            else -> error("Unknown ChatBoostSource type: '$type'")
        }
    }
}