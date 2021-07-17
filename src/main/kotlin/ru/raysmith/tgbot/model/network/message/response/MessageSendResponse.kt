package ru.raysmith.tgbot.model.network.message.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

@Serializable
data class MessageSendResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Message
)

