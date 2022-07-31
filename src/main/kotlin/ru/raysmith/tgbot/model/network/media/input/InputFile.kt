package ru.raysmith.tgbot.model.network.media.input

import java.io.File

fun String.asTgFile() = InputFile.FileIdOrUrl(this)
fun File.asTgFile() = InputFile.File(this)
fun ByteArray.asTgFile(filename: String, mimeType: String) = InputFile.ByteArray(this, filename, mimeType)

sealed class InputFile {
    data class FileIdOrUrl(val value: String) : InputFile()
//    data class Url(val url: String) : TgFile()
    data class File(val file: java.io.File) : InputFile(), NotReusableInputFile
    data class ByteArray(val byteArray: kotlin.ByteArray, val filename: String, val mimeType: String) : InputFile(),
        NotReusableInputFile {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ByteArray

            if (!byteArray.contentEquals(other.byteArray)) return false

            return true
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }
}