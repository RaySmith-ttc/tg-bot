package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class VideoNoteMessage(override val bot: Bot) : MediaMessage(), BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var videoNote: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var length: Int? = null

    override val mediaName: String = "video"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendVideoNote(
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

