package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a boost added to a chat or changed. */
@Serializable
data class ChatBoostUpdated(

    /** Chat which was boosted */
    @SerialName("chat") val chat: Chat,

    /** Information about the chat boost */
    @SerialName("boost") val boost: ChatBoost
)