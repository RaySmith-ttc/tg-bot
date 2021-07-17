package ru.raysmith.tgbot.model.network.updates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatesResult(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val result: List<Update>
)