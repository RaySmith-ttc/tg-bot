package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a boost removed from a chat. */
@Serializable
data class ChatBoostRemoved(

    /** Chat which was boosted */
    val chat: Chat,

    /** Unique identifier of the boost */
    val boostIId: String,

    /** Point in time (Unix timestamp) when the boost was removed */
    val removeDate: Int,

    /** Source of the removed boost */
    val source: ChatBoostSource
)