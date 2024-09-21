package ru.raysmith.tgbot.model.network.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val file: File
)