package ru.raysmith.tgbot.model.api.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a live photo. */
@Serializable
data class LivePhoto(

    /** Available sizes of the corresponding static photo */
    @SerialName("photo") val photo: List<PhotoSize>? = null,

    /** Identifier for the video file which can be used to download or reuse the file */
    @SerialName("file_id") val fileId: String,

    /**
     * Unique identifier for the video file which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     * */
    @SerialName("file_unique_id") val fileUniqueId: String,

    /** Video width as defined by the sender */
    @SerialName("width") val width: Int,

    /** Video height as defined by the sender */
    @SerialName("height") val height: Int,

    /** Duration of the video in seconds as defined by the sender */
    @SerialName("duration") val duration: Int? = null,

    /** MIME type of the file as defined by the sender */
    @SerialName("mime_type") val mimeType: String? = null,

    /** File size in bytes */
    @SerialName("file_size") val fileSize: Long? = null,
)
