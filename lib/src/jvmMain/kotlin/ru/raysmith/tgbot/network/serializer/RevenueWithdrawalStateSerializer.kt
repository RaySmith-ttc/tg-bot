package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypePaid
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
            "pending" -> RevenueWithdrawalStatePendingSerializer
            "succeeded" -> RevenueWithdrawalStateSucceeded.serializer()
            "failed" -> RevenueWithdrawalStateFailedSerializer
            else -> error("Unknown RevenueWithdrawalState type: '$type'")
        }
    }
}

object RevenueWithdrawalStateFailedSerializer : KSerializer<RevenueWithdrawalStateFailed> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RevenueWithdrawalStateFailed") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): RevenueWithdrawalStateFailed {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == RevenueWithdrawalStateFailed.type) {
            "RevenueWithdrawalStateFailed object should have type '${RevenueWithdrawalStateFailed.type}' to deserialize"
        }

        return RevenueWithdrawalStateFailed
    }

    override fun serialize(encoder: Encoder, value: RevenueWithdrawalStateFailed) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, RevenueWithdrawalStateFailed.type)
        }
    }
}

object RevenueWithdrawalStatePendingSerializer : KSerializer<RevenueWithdrawalStatePending> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RevenueWithdrawalStatePending") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): RevenueWithdrawalStatePending {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == RevenueWithdrawalStatePending.type) {
            "RevenueWithdrawalStateFailed object should have type '${RevenueWithdrawalStatePending.type}' to deserialize"
        }

        return RevenueWithdrawalStatePending
    }

    override fun serialize(encoder: Encoder, value: RevenueWithdrawalStatePending) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, RevenueWithdrawalStatePending.type)
        }
    }
}