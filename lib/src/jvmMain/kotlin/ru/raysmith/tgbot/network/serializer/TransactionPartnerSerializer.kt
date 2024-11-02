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
import ru.raysmith.tgbot.model.network.payment.stars.*

object TransactionPartnerSerializer : JsonContentPolymorphicSerializer<TransactionPartner>(TransactionPartner::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<TransactionPartner> {
        val typeObject = element.jsonObject["type"] ?: error("The TransactionPartner json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The TransactionPartner json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The TransactionPartner json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "user" -> TransactionPartnerUser.serializer()
            "fragment" -> TransactionPartnerFragment.serializer()
            "telegram_ads" -> TransactionPartnerTelegramAdsSerializer
            "telegram_api" -> TransactionPartnerTelegramApi.serializer()
            "other" -> TransactionPartnerOtherSerializer
            else -> error("Unknown TransactionPartner type: '$type'")
        }
    }
}

object TransactionPartnerTelegramAdsSerializer : KSerializer<TransactionPartnerTelegramAds> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TransactionPartnerTelegramAds") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): TransactionPartnerTelegramAds {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == TransactionPartnerTelegramAds.type) {
            "RevenueWithdrawalStateFailed object should have type '${TransactionPartnerTelegramAds.type}' to deserialize"
        }

        return TransactionPartnerTelegramAds
    }

    override fun serialize(encoder: Encoder, value: TransactionPartnerTelegramAds) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, TransactionPartnerTelegramAds.type)
        }
    }
}

object TransactionPartnerOtherSerializer : KSerializer<TransactionPartnerOther> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TransactionPartnerOther") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): TransactionPartnerOther {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == TransactionPartnerOther.type) {
            "RevenueWithdrawalStateFailed object should have type '${TransactionPartnerOther.type}' to deserialize"
        }

        return TransactionPartnerOther
    }

    override fun serialize(encoder: Encoder, value: TransactionPartnerOther) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, TransactionPartnerOther.type)
        }
    }
}