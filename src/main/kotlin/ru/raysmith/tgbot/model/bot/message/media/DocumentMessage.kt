package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class DocumentMessage(override val client: HttpClient) : MediaMessageWithThumb() {

    var document: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "document"

    override fun send(chatId: ChatId) = sendDocument(
        chatId = chatId,
        messageThreadId = messageThreadId,
        document = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        thumbnail = thumbnail,
        captionEntities = _caption?.getEntitiesString(),
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}