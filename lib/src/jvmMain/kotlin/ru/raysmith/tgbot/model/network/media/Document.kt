package ru.raysmith.tgbot.model.network.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a general file (as opposed to [photos][PhotoSize], [voice messages][Voice] and [audio files][Audio]). */
@Serializable
data class Document(

    /** Identifier for this file, which can be used to download or reuse the file */
    @SerialName("file_id") override val fileId: String,

    /** Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file. */
    @SerialName("file_unique_id") override val fileUniqueId: String,

    /** Document thumbnail as defined by the sender */
    @SerialName("thumbnail") val thumbnail: PhotoSize? = null,

    /** Original filename as defined by the sender */
    @SerialName("file_name") override val fileName: String? = null,

    /** MIME type of the file as defined by the sender */
    @SerialName("mime_type") val mimeType: String? = null,

    /** File size in bytes. */
    @SerialName("file_size") override val fileSize: Long? = null
) : MediaWithFile