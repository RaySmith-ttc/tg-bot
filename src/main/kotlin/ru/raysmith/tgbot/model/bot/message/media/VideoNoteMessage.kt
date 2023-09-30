package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

class VideoNoteMessage(override val client: HttpClient) : MediaMessage() {

    var videoNote: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var length: Int? = null

    override val mediaName: String = "video"

    override suspend fun send(chatId: ChatId) = sendVideoNote(
        chatId = chatId,
        messageThreadId = messageThreadId,
        videoNote = media ?: error("$mediaName is required"),
        duration = duration,
        length = length,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}

