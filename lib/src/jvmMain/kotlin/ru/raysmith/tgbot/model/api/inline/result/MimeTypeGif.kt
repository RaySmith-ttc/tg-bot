package ru.raysmith.tgbot.model.api.inline.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** MIME type of the thumbnail */
@Serializable
enum class MimeTypeGif {
    @SerialName("image/jpeg") JPEG,
    @SerialName("image/gif") GIF,
    @SerialName("image/mp4") MP4;

    companion object {

        /** Returns supported [MimeTypeGif] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "image/jpeg" -> JPEG
            "image/gif" -> GIF
            "image/mp4" -> MP4
            else -> null
        }
    }
}
