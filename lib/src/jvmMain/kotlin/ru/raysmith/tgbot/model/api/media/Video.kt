package ru.raysmith.tgbot.model.api.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.MediaWithFile

@Serializable
data class Video(
    @SerialName("file_id") override val fileId: String,
    @SerialName("file_unique_id") override val fileUniqueId: String,
    @SerialName("thumbnail") val thumbnail: PhotoSize? = null,
    @SerialName("cover") val cover: List<PhotoSize>? = null,
    @SerialName("start_timestamp") val startTimestamp: Int? = null,
    @SerialName("qualities") val qualities: List<VideoQuality>? = null,
    @SerialName("file_name") override val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") override val fileSize: Long? = null,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("duration") val duration: Int,
) : MediaWithFile
