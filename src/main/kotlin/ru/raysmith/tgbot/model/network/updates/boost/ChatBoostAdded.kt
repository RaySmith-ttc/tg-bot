package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a service message about a user boosting a chat. */
@Serializable
data class ChatBoostAdded(

    /** Number of boosts added by the user */
    @SerialName("boost_count") val boostCount: Int
)
