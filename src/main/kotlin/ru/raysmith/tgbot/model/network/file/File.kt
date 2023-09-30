package ru.raysmith.tgbot.model.network.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a file ready to be downloaded. The file can be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile.
 *
 * Maximum file size to download is 20 MB
 *
 * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
 * */
@Serializable
data class File(

    /** Identifier for this file, which can be used to download or reuse the file  */
    @SerialName("file_id") val id: String,

    /** Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file. */
    @SerialName("file_unique_id") val uniqueId: String,

    /** File size, if known */
    @SerialName("file_size") val size: Long? = null,

    /** File path. Use https://api.telegram.org/file/bot<token>/<file_path> to get the file. */
    @SerialName("file_path") val path: String? = null
) {

    // TODO
//    fun download()
}