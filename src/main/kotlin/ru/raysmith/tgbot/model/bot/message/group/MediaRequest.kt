package ru.raysmith.tgbot.model.bot.message.group

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import java.nio.file.Files

internal data class MultipartPartData(
    val inputFile: InputFile, val name: String, val filename: String, val mimeType: String
)

abstract class MediaRequest {

    internal val inputFiles = mutableListOf<InputFile>()
    protected val multipartBodyParts = mutableListOf<MultipartBody.Part>()

    private fun append(byteArray: ByteArray, name: String, filename: String, mimeType: String): String {
        val requestBody = byteArray.toRequestBody(mimeType.toMediaType())
        multipartBodyParts.add(MultipartBody.Part.createFormData(name, filename, requestBody))
        return "attach://$filename"
    }

    protected fun getMedia(media: InputFile, name: String? = null) = when (media) {
        is InputFile.FileIdOrUrl -> media.value
        is InputFile.ByteArray -> append(media.byteArray, name ?: media.filename, media.filename, media.mimeType)
        is InputFile.File -> append(
            media.file.readBytes(),
            name ?: media.file.nameWithoutExtension,
            media.file.nameWithoutExtension,
            Files.probeContentType(media.file.toPath())
        )
    }.also {
        inputFiles.add(media)
    }

    protected fun getMedia(media: NotReusableInputFile, name: String? = null) = when (media) {
        is InputFile.ByteArray -> append(media.byteArray, name ?: media.filename, media.filename, media.mimeType)
        is InputFile.File -> append(
            media.file.readBytes(),
            name ?: media.file.nameWithoutExtension,
            media.file.nameWithoutExtension,
            Files.probeContentType(media.file.toPath())
        )

        else -> error("ReusableMedia can be only a file or byte array")
    }.also {
        inputFiles.add(media as InputFile)
    }
}