package ru.raysmith.tgbot.model.network.media.input

import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.network.serializer.FileIdOrUrlSerializer
import java.io.File
import java.nio.file.Files

@Suppress("DEPRECATION")
interface Streamable {
    fun input(): Input
}

fun String.asTgFile() = InputFile.FileIdOrUrl(this)

fun File.asTgFile() = InputFile.File(this)
fun ByteArray.asTgFile(filename: String, mimeType: String) = InputFile.ByteArray(this, filename, mimeType)

sealed class InputFile {
    @Serializable(with = FileIdOrUrlSerializer::class)
    data class FileIdOrUrl(val value: String) : InputFile()
    data class File(val file: java.io.File) : InputFile(), NotReusableInputFile, Streamable {
        override fun input() = file.inputStream().asInput()
    }
    data class ByteArray(
        val byteArray: kotlin.ByteArray, val filename: String, val mimeType: String
    ) : InputFile(), NotReusableInputFile, Streamable {
        override fun input() = ByteReadPacket(byteArray)

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

fun NotReusableInputFile.toRequestBody(name: String) = (this as InputFile).toRequestBody(name)
fun InputFile.toRequestBody(name: String) = when(this) {
    is InputFile.ByteArray -> {
        val body = byteArray.toRequestBody(mimeType.toMediaType())
        MultipartBody.Part.createFormData(name, filename, body)
    }
    is InputFile.File -> {
        val mimeType = Files.probeContentType(file.toPath())?.toMediaType()
        val body = file.readBytes().toRequestBody(mimeType)
        MultipartBody.Part.createFormData(name, file.name, body)
    }
    is InputFile.FileIdOrUrl -> MultipartBody.Part.createFormData(name, value)
}