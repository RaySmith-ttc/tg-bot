package ru.raysmith.tgbot.model.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message

@Serializable
data class MessageResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Message
) : LiveLocationResponse
