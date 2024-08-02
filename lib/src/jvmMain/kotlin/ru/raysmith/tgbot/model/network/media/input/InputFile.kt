package ru.raysmith.tgbot.model.network.media.input

import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.FileIdOrUrlSerializer
import java.io.File

interface Streamable {
    fun input(): Input
}

fun String.asTgFile() = InputFile.FileIdOrUrl(this)

fun File.asTgFile() = InputFile.File(this)
fun ByteArray.asTgFile(filename: String, mimeType: String? = null) = InputFile.ByteArray(this, filename, mimeType)

sealed class InputFile {
    @Serializable(with = FileIdOrUrlSerializer::class)
    data class FileIdOrUrl(val value: String) : InputFile()
    data class File(val file: java.io.File) : InputFile(), NotReusableInputFile, Streamable {
        override fun input() = file.inputStream().asInput()
    }
    data class ByteArray(
        val byteArray: kotlin.ByteArray, val filename: String, val mimeType: String? = null
    ) : InputFile(), NotReusableInputFile, Streamable {
        override fun input() = ByteReadPacket(byteArray)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ByteArray
            return byteArray.contentEquals(other.byteArray)
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }
}