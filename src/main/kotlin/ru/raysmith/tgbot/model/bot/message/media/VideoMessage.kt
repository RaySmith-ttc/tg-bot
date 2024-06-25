package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class VideoMessage(override val bot: Bot) : MediaMessageWithThumb(), SpolerableContent {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var hasSpoiler: Boolean? = null
    var video: InputFile?
        get() = media
        set(value) { media = value }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null
    var supportsStreaming: Boolean? = null

    override val mediaName: String = "video"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength

    /** Pass *True*, if the caption must be shown above the message media */
    var showCaptionAboveMedia: Boolean? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendVideo(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        video = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        showCaptionAboveMedia = showCaptionAboveMedia,
        hasSpoiler = hasSpoiler,
        duration = duration,
        width = width,
        height = height,
        thumbnail = thumbnail,
        supportsStreaming = supportsStreaming,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}


