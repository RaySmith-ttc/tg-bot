package ru.raysmith.tgbot.model.api.inline.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** MIME type of the video */
@Serializable
enum class MimeTypeVideo {
    @SerialName("text/html") HTML,
    @SerialName("video/mp4") MP4;

    companion object {

        /** Returns supported [MimeTypeVideo] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "text/html" -> HTML
            "video/mp4" -> MP4
            else -> null
        }
    }
}
