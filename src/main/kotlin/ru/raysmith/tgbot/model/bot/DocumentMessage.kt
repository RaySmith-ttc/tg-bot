package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import java.nio.file.Files

abstract class MediaMessageWithThumb : MediaMessage() {
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

class DocumentMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessageWithThumb() {

    var document: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "document"

    override fun send(chatId: ChatId): MessageResponse = when(document) {
        is InputFile.ByteArray, is InputFile.File -> {
            service.sendDocument(
                chatId = chatId.toRequestBody(),
                document = getMediaMultipartBody(),
                thumb = getThumbMultipartBody(),
                caption = getCaptionText()?.toRequestBody(),
                parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                captionEntities = _caption?.getEntitiesString()?.toRequestBody(),
                disableNotification = disableNotification?.toString()?.toRequestBody(),
                protectContent = protectContent?.toString()?.toRequestBody(),
                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
            ).execute().body() ?: errorBody()
        }
        is InputFile.FileIdOrUrl -> {
            service.sendDocument(
                chatId = chatId,
                document = (media as InputFile.FileIdOrUrl).value,
                caption = getCaptionText(),
                parseMode = parseMode,
                captionEntities = _caption?.getEntitiesString(),
                disableNotification = disableNotification,
                protectContent = protectContent,
                replyToMessageId = replyToMessageId,
                allowSendingWithoutReply = allowSendingWithoutReply,
                keyboardMarkup = keyboardMarkup?.toMarkup()
            ).execute().body() ?: errorBody()
        }
        null -> error("$mediaName is required")
    }
}