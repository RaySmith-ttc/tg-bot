package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import java.nio.file.Files

abstract class MediaMessageWithThumb : CaptionableMediaMessage() {
    var thumbnail: NotReusableInputFile? = null

    protected fun getThumbMultipartBody() = when(thumbnail) {
        is InputFile.ByteArray -> {
            val thumbnail = thumbnail as InputFile.ByteArray
            MultipartBody.Part.createFormData(
                "thumbnail", thumbnail.filename, thumbnail.byteArray.toRequestBody(thumbnail.mimeType.toMediaType())
            )
        }
        is InputFile.File -> {
            val thumbnail = thumbnail as InputFile.File
            MultipartBody.Part.createFormData(
                "thumbnail", thumbnail.file.nameWithoutExtension,
                thumbnail.file.readBytes().toRequestBody(Files.probeContentType(thumbnail.file.toPath()).toMediaType())
            )
        }
        null -> null
        else -> error("Unknown thumb type")
    }
}