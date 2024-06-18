package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.withSafeLength

/** Represent a simple message with a text to be sent or edit using the [sendMessage][API.sendMessage] method */
@TextMessageDsl
class TextMessage(override val bot: Bot) : MessageWithReplyMarkup, BotHolder {
    override val client: HttpClient = bot.client
    override val botConfig: BotConfig = bot.botConfig

    /** Full text of message with entities */
    private var messageText: MessageText? = null

    /** Simple text to send the message, optionally with [parseMode] */
    var text: String? = null

    /** [Parse mode][ParseMode] for a simple text */
    var parseMode: ParseMode? = null

    /** Link preview generation options for the message */
    var linkPreviewOptions = bot.botConfig.linkPreviewOptions

    /** Whether test should be truncated if text length is greater than 4096 */
    var safeTextLength: Boolean = bot.botConfig.safeTextLength

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null

    /** Sets the text as a [MessageText] object */
    @TextMessageDsl
    suspend fun textWithEntities(setText: suspend MessageText.() -> Unit) {
        messageText = MessageText(MessageTextType.TEXT, bot.botConfig)
        messageText!!.apply { setText() }
    }

    /** Returns the raw text with safe length for sending the message, empty string if not set */
    private fun getMessageText() =
        messageText?.getTextString()
            ?: text?.let { if (safeTextLength) it.withSafeLength(MessageTextType.TEXT) else it }
            ?: ""

    /** Returns the [parseMode] if entities were not used, null otherwise */
    private fun getParseModeIfNeed() = if (messageText != null) null else parseMode

    override suspend fun send(chatId: ChatId, messageThreadId: Int?) = sendMessage(
        chatId = chatId,
        messageThreadId = messageThreadId,
        text = getMessageText(),
        parseMode = getParseModeIfNeed(),
        entities = messageText?.getEntitiesString(),
        linkPreviewOptions = linkPreviewOptions,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyParameters = replyParameters,
        keyboardMarkup = keyboardMarkup?.toMarkup()
    )

    override suspend fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = editMessageText(
        chatId = chatId,
        messageId = messageId,
        inlineMessageId = inlineMessageId,
        text = getMessageText(),
        parseMode = getParseModeIfNeed(),
        entities = messageText?.getEntitiesString(),
        linkPreviewOptions = linkPreviewOptions,
        replyMarkup = keyboardMarkup?.toMarkup()
    )

    suspend fun datePicker(datePicker: DatePicker, data: String? = null) {
        textWithEntities { datePickerMessageText(datePicker, data) }
        inlineKeyboard { createDatePicker(datePicker, data) }
    }
}