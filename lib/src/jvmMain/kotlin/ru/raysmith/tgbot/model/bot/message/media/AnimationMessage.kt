package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.BusinessMessage
import ru.raysmith.tgbot.model.bot.message.EffectableMessage
import ru.raysmith.tgbot.model.bot.message.ThreadMessage
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.message.SuggestablePostMessage

class AnimationMessage(override val bot: Bot) : CaptionableMediaMessage(), BotHolder, ThreadMessage,
    EffectableMessage, BusinessMessage, SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig
    var hasSpoiler: Boolean? = null
    var animation: InputFile?
        get() = media
        set(value) {
            media = value
        }

    var duration: Int? = null
    var width: Int? = null
    var height: Int? = null
    var thumbnail: NotReusableInputFile? = null

    override val mediaName: String = "animation"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength
    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var allowPaidBroadcast: Boolean? = null
    override var directMessagesTopicId: Int? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    override suspend fun send(chatId: ChatId): Message = sendAnimation(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        animation = media ?: error("$mediaName is required"),
        duration = duration,
        width = width,
        height = height,
        thumbnail = thumbnail,
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        showCaptionAboveMedia = showCaptionAboveMedia,
        hasSpoiler = hasSpoiler,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )
}
