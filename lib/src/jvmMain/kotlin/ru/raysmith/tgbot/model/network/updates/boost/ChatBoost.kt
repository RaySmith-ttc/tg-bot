package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains information about a chat boost. */
@Serializable
data class ChatBoost(

    /** Unique identifier of the boost */
    @SerialName("boost_id") val boostId: String,

    /** Point in time (Unix timestamp) when the chat was boosted */
    @SerialName("add_date") val addDate: Int,

    /**
     * Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's
     * Telegram Premium subscription is prolonged
     * */
    @SerialName("expiration_date") val expirationDate: Int,

    /** Source of the added boost */
    @SerialName("source") val source: ChatBoostSource
)

