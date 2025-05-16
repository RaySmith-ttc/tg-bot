package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.datepicker.DatePicker

/** Represent a simple message with a text to be sent or edit using the [sendMessage][API.sendMessage] method */
@TextMessageDsl
class TextMessage(
    override val bot: Bot
) : TextWithEntities(bot, MessageTextType.TEXT), MessageWithReplyMarkup, BotHolder, IMessage<Message> {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    /** Link preview generation options for the message */
    var linkPreviewOptions = bot.botConfig.linkPreviewOptions

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null
    override var businessConnectionId: String? = null
    override var messageEffectId: String? = null
    override var allowPaidBroadcast: Boolean? = null

    override suspend fun send(chatId: ChatId) = sendMessage(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageThreadId = messageThreadId,
        text = getMessageText(),
        parseMode = getParseModeIfNeed(),
        entities = messageText?.getEntities(),
        linkPreviewOptions = linkPreviewOptions,
        disableNotification = disableNotification,
        protectContent = protectContent,
        messageEffectId = messageEffectId,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = editMessageText(
        businessConnectionId = businessConnectionId,
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        text = getMessageText(),
        parseMode = getParseModeIfNeed(),
        entities = messageText?.getEntities(),
        linkPreviewOptions = linkPreviewOptions,
        replyMarkup = keyboardMarkup?.toMarkup()
    )

    context(_: BotContext<*>)
    suspend fun datePicker(datePicker: DatePicker, data: String? = null) {
        textWithEntities { datePickerMessageText(datePicker, data) }
        inlineKeyboard { createDatePicker(datePicker, data) }
    }
}