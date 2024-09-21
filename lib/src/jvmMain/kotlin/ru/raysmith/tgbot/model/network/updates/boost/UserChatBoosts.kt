package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a list of boosts added to a chat by a user. */
@Serializable
data class UserChatBoosts(

    /** The list of boosts added to the chat by the user */
    @SerialName("boosts") val boosts: List<ChatBoost>
)
