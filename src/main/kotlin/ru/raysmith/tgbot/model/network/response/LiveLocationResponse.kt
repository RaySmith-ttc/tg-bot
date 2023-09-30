package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

// TODO prolem: NetworkResponse<Boolean> not inheritance LiveLocationResponse
@Serializable(with = LiveLocationResponseSerializer::class)
sealed class LiveLocationResponse

object LiveLocationResponseSerializer : JsonContentPolymorphicSerializer<LiveLocationResponse>(LiveLocationResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out LiveLocationResponse> {
        return when(element.jsonObject["result"]) {
            is JsonObject ->  MessageResponse.serializer()
            is JsonPrimitive -> BooleanResponse.serializer()
            else -> error("Unexpected LiveLocationResponse result")
        }
    }
}