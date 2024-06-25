package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class PhotoMessage(override val bot: Bot) : CaptionableMediaMessage(), SpolerableContent {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    override var hasSpoiler: Boolean? = null
    var photo: InputFile?
        get() = media
        set(value) { media = value }

    override val mediaName: String = "photo"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength

    /** Pass *True*, if the caption must be shown above the message media */
    var showCaptionAboveMedia: Boolean? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendPhoto(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        photo = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = parseMode,
        captionEntities = _caption?.getEntities(),
        showCaptionAboveMedia = showCaptionAboveMedia,
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        replyMarkup = keyboardMarkup?.toMarkup()
    )
}