package ru.raysmith.tgbot.model.network.media.input

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.nio.file.Files

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

fun InputFile.toRequestBody(name: String) = when(this) {
    is InputFile.ByteArray -> {
        val body = byteArray.toRequestBody(mimeType.toMediaType())
        MultipartBody.Part.createFormData(name, filename, body)
    }
    is InputFile.File -> {
        val mimeType = Files.probeContentType(file.toPath()).toMediaType()
        val body = file.readBytes().toRequestBody(mimeType)
        MultipartBody.Part.createFormData(name, file.name, body)
    }
    is InputFile.FileIdOrUrl -> MultipartBody.Part.createFormData(name, value)
}