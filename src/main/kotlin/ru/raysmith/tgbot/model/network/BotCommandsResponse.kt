package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.command.BotCommand

@Serializable
data class BotCommandsResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: List<BotCommand>
)