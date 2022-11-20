package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IntResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: Int
)