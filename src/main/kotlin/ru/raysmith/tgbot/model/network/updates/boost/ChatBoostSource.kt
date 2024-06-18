package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.ChatBoostSourceSerializer

/**
 * This object describes the source of a chat boost. It can be one of
 *
 * - [ChatBoostSourcePremium]
 * - [ChatBoostSourceGiftCode]
 * - [ChatBoostSourceGiveaway]
 * */
@Polymorphic
@Serializable(with = ChatBoostSourceSerializer::class)
sealed class ChatBoostSource {

    /** Source of the boost */
    abstract val source: String
}

