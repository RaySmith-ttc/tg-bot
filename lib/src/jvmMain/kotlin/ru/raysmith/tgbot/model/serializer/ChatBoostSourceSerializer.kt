package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.updates.boost.ChatBoostSource
import ru.raysmith.tgbot.model.api.updates.boost.ChatBoostSourceGiftCode
import ru.raysmith.tgbot.model.api.updates.boost.ChatBoostSourceGiveaway
import ru.raysmith.tgbot.model.api.updates.boost.ChatBoostSourcePremium

internal object ChatBoostSourceSerializer :
    ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<ChatBoostSource>(ChatBoostSource::class, "source") {

    override fun selectDeserializer(type: String) = when (type) {
        "premium" -> ChatBoostSourcePremium.serializer()
        "gift_code" -> ChatBoostSourceGiftCode.serializer()
        "giveaway" -> ChatBoostSourceGiveaway.serializer()
        else -> null
    }

}
