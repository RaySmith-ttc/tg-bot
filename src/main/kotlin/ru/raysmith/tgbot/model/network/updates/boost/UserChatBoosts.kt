package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Serializable

/** This object represents a list of boosts added to a chat by a user. */
@Serializable
data class UserChatBoosts(

    /** The list of boosts added to the chat by the user */
    val boosts: List<ChatBoost>
)
