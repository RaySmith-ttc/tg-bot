package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** Type of the chat, from which the inline query was sent */
enum class ChatType {
    @SerialName("sender") SENDER,
    @SerialName("private") PRIVATE,
    @SerialName("group") GROUP,
    @SerialName("supergroup") SUPERGROUP,
    @SerialName("channel") CHANNEL
}