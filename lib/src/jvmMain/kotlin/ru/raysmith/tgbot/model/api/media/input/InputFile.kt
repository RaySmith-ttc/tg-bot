package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.PhotoSize
import ru.raysmith.tgbot.model.api.media.Video
import ru.raysmith.tgbot.model.serializer.FileIdOrUrlSerializer
import ru.raysmith.tgbot.network.API

/**
 * # Sending files
 * There are three ways to send files (photos, stickers, audio, media, etc.):
 *
 * 1. If the file is already stored somewhere on the Telegram servers, you don't need to reupload it: each file object
 * has a **fileId** field, simply pass this **fileId** as a [FileIdOrUrl] parameter instead of uploading.
 * There are **no limits** for files sent this way.
 * 2. Provide Telegram [FileIdOrUrl] with an HTTP URL for the file to be sent. Telegram will download and send the file.
 * 5 MB max size for photos and 20 MB max for other types of content.
 * 3. Post the file using [File] or [ByteArray]. 10 MB max size for photos, 50 MB for other files.
 *
 * ## Sending by fileId
 *
 * - It is not possible to change the file type when resending by **fileId**. I.e. a [video][Video] can't be
 * [sent as a photo][API.sendPhoto], a [photo][PhotoSize] can't be [sent as a document][API.sendDocument], etc.
 * - It is not possible to resend thumbnails.
 * - Resending a photo by **fileId** will send all of its [sizes][PhotoSize].
 * - **fileId** is unique for each individual bot and **can't** be transferred from one bot to another.
 * - **fileId** uniquely identifies a file, but a file can have different valid **fileId**s even for the same bot.
 *
 * ## Sending by URL
 *
 * - When sending by URL the target file must have the correct MIME type
 * (e.g., audio/mpeg for [sendAudio][API.sendAudio], etc.).
 * - In [sendDocument][API.sendDocument], sending by URL will currently only work for **.PDF** and **.ZIP** files.
 * - To use [sendVoice][API.sendVoice], the file must have the type audio/ogg and be no more than 1MB in size.
 * 1-20MB voice notes will be sent as files.
 * - Other configurations may work but we can't guarantee that they will.
 * */
sealed interface InputFile {

    /**
     * This object represents a file that is already stored on the Telegram servers or a file that can be downloaded via HTTP URL.
     *
     * @property value The file identifier or HTTP URL of the file to be sent.
     * */
    @Serializable(FileIdOrUrlSerializer::class)
    data class FileIdOrUrl(val value: String) : InputFile // TODO split

    /**
     * This object represents a file to be uploaded.
     *
     * @property file The file to be uploaded.
     * @property mimeType The MIME type of the file to be uploaded. If null, the MIME type will be determined
     *  automatically.
     * */
    data class File(
        val file: java.io.File,
        val mimeType: String? = null,
    ) : InputFile, NotReusableInputFile

    /**
     * This object represents a file to be uploaded.
     *
     * @property stream The input stream to be uploaded.
     * @property mimeType The MIME type of the file to be uploaded. If null, the MIME type will be determined
     *  automatically.
     * */
    data class InputStream(
        val stream: java.io.InputStream,
        val filename: String,
        val mimeType: String? = null,
    ) : InputFile, NotReusableInputFile

    /**
     * This object represents a file to be uploaded as a byte array.
     *
     * @property byteArray The byte array to be uploaded.
     * @property filename The name of the file to be uploaded.
     * @property mimeType The MIME type of the file to be uploaded. If null, the MIME type will be determined
     *  automatically.
     * */
    data class ByteArray(
        val byteArray: kotlin.ByteArray,
        val filename: String,
        val mimeType: String? = null,
    ) : InputFile, NotReusableInputFile {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ByteArray) return false

            return byteArray.contentEquals(other.byteArray) && filename == other.filename && mimeType == other.mimeType
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode() + filename.hashCode() + mimeType.hashCode()
        }
    }
}
