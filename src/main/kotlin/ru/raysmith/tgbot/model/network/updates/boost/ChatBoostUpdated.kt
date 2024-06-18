package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a boost added to a chat or changed. */
@Serializable
data class ChatBoostUpdated(

    /** Chat which was boosted */
    val chat: Chat,

    /** Information about the chat boost */
    val boost: ChatBoost
)