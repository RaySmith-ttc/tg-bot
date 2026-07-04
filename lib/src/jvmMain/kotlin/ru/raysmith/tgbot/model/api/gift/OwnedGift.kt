package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.OwnedGiftSerializer

/**
 * This object describes a gift received and owned by a user or a chat. Currently, it can be one of
 *
 * - [OwnedGiftRegular]
 * - [OwnedGiftUnique]
 * */
@Serializable(OwnedGiftSerializer::class)
sealed interface OwnedGift {

    /** Type of the gift */
    val type: String
}
