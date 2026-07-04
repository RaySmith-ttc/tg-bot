package ru.raysmith.tgbot.model.api.inline.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** MIME type of the content of the file */
@Serializable
enum class MimeTypeDocument {
    @SerialName("application/pdf") PDF,
    @SerialName("application/zip") ZIP;

    companion object {

        /** Returns supported [MimeTypeDocument] of [mimeType] param or null if not found */
        fun of(mimeType: String) = when(mimeType) {
            "application/pdf" -> PDF
            "application/zip" -> ZIP
            else -> null
        }
    }
}
