package ru.raysmith.tgbot.model.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.File

@Serializable
data class FileResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("result") val file: File,
)
