package ru.raysmith.tgbot.model.api.response

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(LiveLocationResponseSerializer::class)
sealed interface LiveLocationResponse

object LiveLocationResponseSerializer : JsonContentPolymorphicSerializer<LiveLocationResponse>(LiveLocationResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LiveLocationResponse> {
        return when(element.jsonObject["result"]) {
            is JsonObject ->  MessageResponse.serializer()
            is JsonPrimitive -> BooleanResponse.serializer()
            else -> error("Unexpected LiveLocationResponse result")
        }
    }
}
