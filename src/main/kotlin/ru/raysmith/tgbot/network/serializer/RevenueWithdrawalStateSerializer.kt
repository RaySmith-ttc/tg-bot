package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.payment.stars.RevenueWithdrawalState
import ru.raysmith.tgbot.model.network.payment.stars.RevenueWithdrawalStateFailed
import ru.raysmith.tgbot.model.network.payment.stars.RevenueWithdrawalStatePending
import ru.raysmith.tgbot.model.network.payment.stars.RevenueWithdrawalStateSucceeded

object RevenueWithdrawalStateSerializer : JsonContentPolymorphicSerializer<RevenueWithdrawalState>(RevenueWithdrawalState::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RevenueWithdrawalState> {
        val typeObject = element.jsonObject["type"] ?: error("The RevenueWithdrawalState json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The RevenueWithdrawalState json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The RevenueWithdrawalState json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "pending" -> RevenueWithdrawalStatePending.serializer()
            "succeeded" -> RevenueWithdrawalStateSucceeded.serializer()
            "failed" -> RevenueWithdrawalStateFailed.serializer()
            else -> error("Unknown RevenueWithdrawalState type: '$type'")
        }
    }
}