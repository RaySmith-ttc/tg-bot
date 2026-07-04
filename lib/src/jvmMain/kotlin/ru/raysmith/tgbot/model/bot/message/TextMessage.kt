package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.message.rich.InputRichMessage
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.datepicker.DatePicker

/** Represent a simple message with a text to be sent or edit using the [sendMessage][API.sendMessage] method */
@MessageDsl
class TextMessage(
    override val bot: Bot
) : TextWithEntities(bot, MessageTextType.TEXT), MessageWithReplyMarkup, BotHolder, IMessage<Message>, ThreadMessage,
    SuggestablePostMessage, BusinessMessage, EffectableMessage {

    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    /** Link preview generation options for the message */
    var linkPreviewOptions = bot.botConfig.linkPreviewOptions

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null
    override var businessConnectionId: String? = null // TODO global configuration?
    override var messageEffectId: String? = null
    override var allowPaidBroadcast: Boolean? = null
    override var directMessagesTopicId: Int? = null
    override var suggestedPostParameters: SuggestedPostParameters? = null

    var richMessage: InputRichMessage? = null

    fun richMessage(builder: InputRichMessageBuilder.() -> Unit) {
        richMessage = InputRichMessageBuilder().apply(builder).build()
    }

    override suspend fun send(chatId: ChatId) = if (richMessage != null) {
        sendRichMessage(
            businessConnectionId = businessConnectionId,
            chatId = chatId,
            messageThreadId = messageThreadId,
            directMessagesTopicId = directMessagesTopicId,
            richMessage = richMessage!!,
            disableNotification = disableNotification,
            protectContent = protectContent,
            allowPaidBroadcast = allowPaidBroadcast,
            messageEffectId = messageEffectId,
            suggestedPostParameters = suggestedPostParameters,
            replyParameters = replyParameters,
            replyMarkup = keyboardMarkup?.toMarkup(),
        )
    } else {
        sendMessage(
            businessConnectionId = businessConnectionId,
            chatId = chatId,
            messageThreadId = messageThreadId,
            directMessagesTopicId = directMessagesTopicId,
            text = getMessageText(),
            parseMode = getParseModeIfNeed(),
            entities = messageText?.getEntities(),
            linkPreviewOptions = linkPreviewOptions,
            disableNotification = disableNotification,
            protectContent = protectContent,
            allowPaidBroadcast = allowPaidBroadcast,
            messageEffectId = messageEffectId,
            suggestedPostParameters = suggestedPostParameters,
            replyParameters = replyParameters,
            replyMarkup = keyboardMarkup?.toMarkup(),
        )
    }

    override suspend fun edit(
        chatId: ChatId?,
        messageId: Int?,
        inlineMessageId: String?,
        businessConnectionId: String?,
    ) = editMessageText(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        text = getMessageText(),
        parseMode = getParseModeIfNeed(),
        entities = messageText?.getEntities(),
        linkPreviewOptions = linkPreviewOptions,
        richMessage = richMessage,
        replyMarkup = keyboardMarkup?.toMarkup()
    )

    context(_: BotContext<*>)
    suspend fun datePicker(datePicker: DatePicker, data: String? = null) {
        textWithEntities { datePickerMessageText(datePicker, data) }
        inlineKeyboard { createDatePicker(datePicker, data) }
    }
}
