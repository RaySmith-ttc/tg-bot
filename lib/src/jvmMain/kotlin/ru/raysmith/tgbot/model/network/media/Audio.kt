package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    @SerialName("file_id") override val fileId: String,
    @SerialName("file_unique_id") override val fileUniqueId: String,
    @SerialName("duration") val duration: Int,
    @SerialName("performer") val performer: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("file_name") override val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") override val fileSize: Long? = null,
    @SerialName("thumbnail") val thumbnail: PhotoSize? = null,
) : MediaWithFile