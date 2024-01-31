package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

@Suppress("MemberVisibilityCanBePrivate")
class AudioMessage(override val client: HttpClient) : MediaMessageWithThumb() {

    var audio: InputFile?
        get() = media
        set(value) { media = value }

    var title: String? = null
    var duration: Int? = null
    var performer: String? = null

    override val mediaName: String = "audio"

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendAudio(
        chatId = chatId,
        messageThreadId = messageThreadId,
        audio = audio ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntitiesString(),
        duration = duration,
        performer = performer,
        title = title,
        thumbnail = thumbnail,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = keyboardMarkup?.toMarkup()
    )
}

