package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile

class VoiceMessage(override val bot: Bot) : CaptionableMediaMessage() {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var voice: InputFile?
        get() = media
        set(value) { media = value }

    var title: String? = null
    var duration: Int? = null
    var performer: String? = null

    override val mediaName: String = "audio"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength
    override var businessConnectionId: String? = null

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendVoice(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        voice = media ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        duration = duration,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}