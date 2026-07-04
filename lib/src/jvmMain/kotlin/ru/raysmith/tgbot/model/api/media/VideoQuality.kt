package ru.raysmith.tgbot.model.api.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a video file of a specific quality. */
@Serializable
data class VideoQuality(

    /** Identifier for this file, which can be used to download or reuse the file */
    @SerialName("file_id") val fileId: String,

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     * */
    @SerialName("file_unique_id") val fileUniqueId: String,

    /** Video width */
    @SerialName("width") val width: Int,

    /** Video height */
    @SerialName("height") val height: Int,

    /** Codec that was used to encode the video, for example, “h264”, “h265”, or “av01” */
    @SerialName("codec") val codec: String,

    /** File size in bytes */
    @SerialName("file_size") val fileSize: Long? = null,
)
