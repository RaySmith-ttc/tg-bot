package ru.raysmith.tgbot.model.bot.message.media

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.BusinessMessage
import ru.raysmith.tgbot.model.bot.message.EffectableMessage
import ru.raysmith.tgbot.model.bot.message.SuggestablePostMessage
import ru.raysmith.tgbot.model.bot.message.ThreadMessage

class DocumentMessage(override val bot: Bot) : CaptionableMediaMessage(), ThreadMessage,
    EffectableMessage, BusinessMessage, SuggestablePostMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    var document: InputFile?
        get() = media
        set(value) { media = value }
    var thumbnail: NotReusableInputFile? = null

    override val mediaName: String = "document"
    override var sendChatAction: Boolean = bot.botConfig.sendChatActionWithMedaMessage
    override var safeTextLength: Boolean = bot.botConfig.safeTextLength
    override var messageThreadId: Int? = null
    override var messageEffectId: String? = null
    override var businessConnectionId: String? = null
    override var allowPaidBroadcast: Boolean? = null
    override var directMessagesTopicId: Int? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    var disableContentTypeDetection: Boolean? = null

    override suspend fun send(chatId: ChatId) = sendDocument(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        directMessagesTopicId = directMessagesTopicId,
        document = media ?: error("$mediaName is required"),
        thumbnail = thumbnail,
        caption = getCaptionText(),
        parseMode = getParseModeIfNeed(),
        captionEntities = getEntities(),
        disableContentTypeDetection = disableContentTypeDetection,
        disableNotification = disableNotification,
        protectContent = protectContent,
        allowPaidBroadcast = allowPaidBroadcast,
        messageEffectId = messageEffectId,
        suggestedPostParameters = suggestedPostParameters,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup(),
    )
}
