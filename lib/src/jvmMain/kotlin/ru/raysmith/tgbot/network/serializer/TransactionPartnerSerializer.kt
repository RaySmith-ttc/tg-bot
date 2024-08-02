package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
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
            "telegram_ads" -> TransactionPartnerTelegramAds.serializer()
            "other" -> TransactionPartnerOther.serializer()
            else -> error("Unknown TransactionPartner type: '$type'")
        }
    }
}