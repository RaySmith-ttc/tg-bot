package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.model.api.payment.stars.TransactionPartnerTelegramAds

internal object TransactionPartnerTelegramAdsSerializer : KSerializer<TransactionPartnerTelegramAds> {
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
