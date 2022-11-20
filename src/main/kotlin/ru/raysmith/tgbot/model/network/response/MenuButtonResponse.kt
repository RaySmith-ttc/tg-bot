package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.menubutton.MenuButton

// TODO replace all old responses
@Serializable
data class NetworkResponse<T>(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: T
)

@Serializable
data class MenuButtonResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: MenuButton
)