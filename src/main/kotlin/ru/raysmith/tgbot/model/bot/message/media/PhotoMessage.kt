package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class PhotoMessage(override val client: HttpClient) : CaptionableMediaMessage(), SpolerableContent {

    override var hasSpoiler: Boolean? = null
    var photo: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "photo"

    override suspend fun send(chatId: ChatId) = sendPhoto(
        chatId = chatId,
        messageThreadId = messageThreadId,
        photo = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntitiesString(),
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        replyMarkup = keyboardMarkup?.toMarkup()
    )
}