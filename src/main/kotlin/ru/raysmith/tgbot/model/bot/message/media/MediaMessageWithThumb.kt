package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import java.nio.file.Files

abstract class MediaMessageWithThumb : CaptionableMediaMessage() {
    var thumb: NotReusableInputFile? = null

    protected fun getThumbMultipartBody() = when(thumb) {
        is InputFile.ByteArray -> {
            val thumb = thumb as InputFile.ByteArray
            MultipartBody.Part.createFormData(
                "thumb", thumb.filename, thumb.byteArray.toRequestBody(thumb.mimeType.toMediaType())
            )
        }
        is InputFile.File -> {
            val thumb = thumb as InputFile.File
            MultipartBody.Part.createFormData(
                "thumb", thumb.file.nameWithoutExtension,
                thumb.file.readBytes().toRequestBody(Files.probeContentType(thumb.file.toPath()).toMediaType())
            )
        }
        null -> null
        else -> error("Unknown thumb type")
    }
}