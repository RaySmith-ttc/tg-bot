package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class VoiceMessage(override val bot: Bot) : MediaMessageWithThumb() {
    override val client: HttpClient = bot.client

    var voice: InputFile?
        get() = media
        set(value) { media = value }

    var title: String? = null
    var duration: Int? = null
    var performer: String? = null

    override val mediaName: String = "audio"
    override var sendChatAction: Boolean = bot.config.sendChatActionWithMedaMessage

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendVoice(
        chatId = chatId,
        messageThreadId = messageThreadId,
        voice = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntitiesString(),
        duration = duration,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyToMessageId = replyToMessageId,
        allowSendingWithoutReply = allowSendingWithoutReply,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}