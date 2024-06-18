package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.model.bot.ChatId

object ChatIdSerializer : JsonContentPolymorphicSerializer<ChatId>(ChatId::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatId> {
        return if (element.jsonPrimitive.isString) ChatId.UsernameSerializer else ChatId.IDSerializer
    }
}