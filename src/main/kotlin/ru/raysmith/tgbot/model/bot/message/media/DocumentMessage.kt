package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class DocumentMessage(override val bot: Bot) : MediaMessageWithThumb() {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var document: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "document"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendDocument(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        document = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        thumbnail = thumbnail,
        captionEntities = _caption?.getEntities(),
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}