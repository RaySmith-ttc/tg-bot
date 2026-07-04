package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.gift.OwnedGift
import ru.raysmith.tgbot.model.api.gift.OwnedGiftRegular
import ru.raysmith.tgbot.model.api.gift.OwnedGiftUnique

internal object OwnedGiftSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<OwnedGift>(OwnedGift::class) {
    override fun selectDeserializer(type: String) = when (type) {
        "regular" -> OwnedGiftRegular.serializer()
        "unique" -> OwnedGiftUnique.serializer()
        else -> null
    }
}
