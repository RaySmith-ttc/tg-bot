package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a boost removed from a chat. */
@Serializable
data class ChatBoostRemoved(

    /** Chat which was boosted */
    @SerialName("chat") val chat: Chat,

    /** Unique identifier of the boost */
    @SerialName("boost_id") val boostId: String,

    /** Point in time (Unix timestamp) when the boost was removed */
    @SerialName("remove_date") val removeDate: Int,

    /** Source of the removed boost */
    @SerialName("source") val source: ChatBoostSource
)