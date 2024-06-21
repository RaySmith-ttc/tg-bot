package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.Message

class AnimationMessage(override val bot: Bot) : MediaMessageWithThumb(), SpolerableContent, BotHolder {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    override var hasSpoiler: Boolean? = null
    var animation: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null

    override val mediaName: String = "animation"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength

    override suspend fun send(chatId: ChatId, messageThreadId: Int?): Message = sendAnimation(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId ?: this.messageThreadId,
        animation = media ?: error("$mediaName is required"),
        duration = duration,
        width = width,
        height = height,
        thumbnail = thumbnail,
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntities(),
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}