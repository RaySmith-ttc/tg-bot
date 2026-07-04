package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.payment.stars.*

internal object TransactionPartnerSerializer :
    BaseJsonContentPolymorphicSerializer<TransactionPartner>(TransactionPartner::class) {

    override fun selectDeserializer(type: String) = when (type) {
        "user" -> TransactionPartnerUser.serializer()
        "chat" -> TransactionPartnerChat.serializer()
        "affiliate_program" -> TransactionPartnerAffiliateProgram.serializer()
        "fragment" -> TransactionPartnerFragment.serializer()
        "telegram_ads" -> TransactionPartnerTelegramAds.serializer()
        "telegram_api" -> TransactionPartnerTelegramApi.serializer()
        "other" -> TransactionPartnerOther.serializer()
        else -> null
    }
}
