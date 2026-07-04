package ru.raysmith.tgbot.model.api.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.MediaWithFile

@Serializable
data class Voice(
    @SerialName("file_id") override val fileId: String,
    @SerialName("file_unique_id") override val fileUniqueId: String,
    @SerialName("file_name") override val fileName: String? = null,
    @SerialName("file_size") override val fileSize: Long? = null,
    @SerialName("duration") val duration: Int,
) : MediaWithFile
