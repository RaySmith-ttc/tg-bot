package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
data class GetMeResponse(
    @SerialName("ok")
    val ok: Boolean,
    @SerialName("result")
    val result: User
)