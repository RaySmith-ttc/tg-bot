package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Voice(
    @SerialName("file_id") override val fileId: String,
    @SerialName("file_unique_id") override val fileUniqueId: String,
    @SerialName("file_name") override val fileName: String? = null,
    @SerialName("file_size") override val fileSize: Int? = null,
    @SerialName("duration") val duration: Int,
) : Media