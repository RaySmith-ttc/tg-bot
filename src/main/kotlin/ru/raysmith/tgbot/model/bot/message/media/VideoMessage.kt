package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class VideoMessage(override val bot: Bot) : MediaMessageWithThumb(), SpolerableContent {
    override val client: HttpClient = bot.client

    override var hasSpoiler: Boolean? = null
    var video: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null
    var supportsStreaming: Boolean? = null

    override val mediaName: String = "video"
    override var sendChatAction: Boolean = bot.config.sendChatActionWithMedaMessage

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendVideo(
        chatId = chatId,
        messageThreadId = messageThreadId,
        video = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntitiesString(),
        hasSpoiler = hasSpoiler,
        duration = duration,
        width = width,
        height = height,
        supportsStreaming = supportsStreaming,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}


