package ru.raysmith.tgbot.model.network.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetChatResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Chat
)