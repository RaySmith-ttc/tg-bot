package ru.raysmith.tgbot.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO replace all old responses
@Serializable
data class NetworkResponse<T>(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: T
)