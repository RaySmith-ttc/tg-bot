package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageId

interface MessageRes

@Serializable
data class MessageResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Message
) : MessageRes, LiveLocationResponse()

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

