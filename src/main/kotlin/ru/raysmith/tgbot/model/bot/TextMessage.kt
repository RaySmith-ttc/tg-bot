package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.withSafeLength

@DslMarker
annotation class TextMessageDsl

/** Represent a simple message with a text to be sent or edit using the [sendMessage][TelegramService.sendMessage] method */
@TextMessageDsl
class TextMessage(override val service: TelegramService, override val fileService: TelegramFileService) : EditableMessage, KeyboardCreator {

    /** Full text of message with entities */
    private var messageText: MessageText? = null

    /** Simple text to send the message, optionally with [parseMode] */
    var text: String? = null

    /** [Parse mode][ParseMode] for a simple text */
    var parseMode: ParseMode? = null

    /** Disables link previews for links in this message */
    var disableWebPagePreview: Boolean? = null

    /** Whether test should be truncated if text length is greater than 4096 */
    var safeTextLength: Boolean = Bot.Config.safeTextLength

    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null

    /** Sets the text as a [MessageText] object */
    @TextMessageDsl
    fun textWithEntities(setText: MessageText.() -> Unit) {
        messageText = MessageText(MessageTextType.TEXT)
        messageText!!.apply(setText)
    }

    /** Returns the raw text with safe length for sending the message, empty string if not set */
    private fun getMessageText() =
        messageText?.getTextString()
            ?: text?.let { if (safeTextLength) it.withSafeLength(MessageTextType.TEXT) else it }
            ?: ""

    /** Returns the [parseMode] if entities were not used, null otherwise */
    private fun getParseModeIfNeed() = if (messageText != null) null else parseMode

    override fun send(chatId: ChatId): MessageResponse {
        return service.sendMessage(
            chatId = chatId,
            text = getMessageText(),
            parseMode = getParseModeIfNeed(),
            entities = messageText?.getEntitiesString(),
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            protectContent = protectContent,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute().body() ?: errorBody()
    }

    override fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) : MessageResponse {
        return service.editMessageText(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            text = getMessageText(),
            parseMode = getParseModeIfNeed(),
            entities = messageText?.getEntitiesString(),
            replyMarkup = keyboardMarkup?.toMarkup(),
            disableWebPagePreview = disableWebPagePreview
        ).execute().body() ?: errorBody()
    }
}