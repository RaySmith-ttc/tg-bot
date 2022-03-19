package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMeResponse(
    @SerialName("ok")
    val ok: Boolean,
    @SerialName("result")
    val result: User
)