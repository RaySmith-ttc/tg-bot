package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

interface MessageRes

@Serializable
data class MessageResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Message
) : MessageRes

@Serializable
data class MessageIdResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: MessageId
) : MessageRes

@Serializable
data class MessageResponseArray(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val results: List<Message>
) : MessageRes

