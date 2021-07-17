package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoNote(
    @SerialName("file_id") override val fileId: String,
    @SerialName("file_unique_id") override val fileUniqueId: String,
    @SerialName("thumb") val thumb: PhotoSize? = null,
    @SerialName("file_name") override val fileName: String? = null,
    @SerialName("file_size") override val fileSize: Int? = null,
    @SerialName("length") val length: Int,
    @SerialName("duration") val duration: Int,
) : Media