package ru.raysmith.tgbot.model.bot.message.media

import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class VideoNoteMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaMessage() {

    var videoNote: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var length: Int? = null

    override val mediaName: String = "video"

    override fun send(chatId: ChatId): MessageResponse = when(videoNote) {
        is InputFile.ByteArray, is InputFile.File -> {
            service.sendVideoNote(
                chatId = chatId.toRequestBody(),
                messageThreadId = messageThreadId?.toString()?.toRequestBody(),
                videoNote = getMediaMultipartBody(),
                duration = duration?.toString()?.toRequestBody(),
                length = length?.toString()?.toRequestBody(),
                disableNotification = disableNotification?.toString()?.toRequestBody(),
                protectContent = protectContent?.toString()?.toRequestBody(),
                replyToMessageId = replyToMessageId?.toString()?.toRequestBody(),
                allowSendingWithoutReply = allowSendingWithoutReply?.toString()?.toRequestBody(),
                keyboardMarkup = keyboardMarkup?.toJson()?.toRequestBody()
            ).execute().body() ?: errorBody()
        }
        is InputFile.FileIdOrUrl -> {
            service.sendVideoNote(
                chatId = chatId,
                messageThreadId = messageThreadId,
                videoNote = (media as InputFile.FileIdOrUrl).value,
                duration = duration,
                length = length,
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

