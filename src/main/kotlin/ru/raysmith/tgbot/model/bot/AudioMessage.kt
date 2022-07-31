package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.encodeToString
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class AudioMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessageWithThumb() {

    var audio: InputFile?
        get() = media
        set(value) { media = value }

    var title: String? = null
    var duration: Int? = null
    var performer: String? = null

    override val mediaName: String = "audio"

    override fun send(chatId: ChatId): MessageResponse = when(media) {
        is InputFile.ByteArray, is InputFile.File -> {
            service.sendAudio(
                chatId = chatId.toRequestBody(),
                audio = getMediaMultipartBody(),
                caption = getCaptionText()?.toRequestBody(),
                parseMode = parseMode?.let { TelegramApi.json.encodeToString(it) }?.toRequestBody(),
                captionEntities = _caption?.getEntitiesString(safeTextLength)?.toRequestBody(),
                duration = duration?.toString()?.toRequestBody(),
                performer = performer?.toRequestBody(),
                title = title?.toRequestBody(),
                thumb = getThumbMultipartBody(),
                disableNotification = disableNotification?.toString()?.toRequestBody(),
                protectContent = protectContent?.toString()?.toRequestBody(),
                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
            ).execute().body() ?: errorBody()
        }
        is InputFile.FileIdOrUrl -> {
            service.sendAudio(
                chatId = chatId,
                audio = (media as InputFile.FileIdOrUrl).value,
                caption = getCaptionText(),
                parseMode = parseMode,
                captionEntities = _caption?.getEntitiesString(safeTextLength),
                duration = duration,
                performer = performer,
                title = title,
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