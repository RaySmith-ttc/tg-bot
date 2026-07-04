package ru.raysmith.tgbot.model.api.updates.boost

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.ChatBoostSourceSerializer

/**
 * This object describes the source of a chat boost. It can be one of
 *
 * - [ChatBoostSourcePremium]
 * - [ChatBoostSourceGiftCode]
 * - [ChatBoostSourceGiveaway]
 * */
@Serializable(ChatBoostSourceSerializer::class)
sealed interface ChatBoostSource {

    /** Source of the boost */
    val source: String
}

