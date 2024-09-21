package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

@Serializable
data class MessageResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Message
) : LiveLocationResponse()
