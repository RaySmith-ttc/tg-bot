package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.BusinessMessage
import ru.raysmith.tgbot.model.bot.message.EffectableMessage
import ru.raysmith.tgbot.model.bot.message.ThreadMessage
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.message.SuggestablePostMessage

@Suppress("MemberVisibilityCanBePrivate")
class AudioMessage(override val bot: Bot) : CaptionableMediaMessage(), ThreadMessage, EffectableMessage, BusinessMessage,
    SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var audio: InputFile?
        get() = media
        set(value) {
            media = value
        }

    var title: String? = null
    var duration: Int? = null
    var performer: String? = null
    var thumbnail: NotReusableInputFile? = null

    override val mediaName: String = "audio"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength
    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    override suspend fun send(chatId: ChatId) = sendAudio(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        audio = audio ?: error("$mediaName is required"),
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        duration = duration,
        performer = performer,
        title = title,
        thumbnail = thumbnail,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        replyMarkup = keyboardMarkup?.toMarkup(),
    )
}
