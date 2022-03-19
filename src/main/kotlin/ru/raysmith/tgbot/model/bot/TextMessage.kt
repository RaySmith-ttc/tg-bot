package ru.raysmith.tgbot.model.bot

import retrofit2.Response
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.withSafeLength

@DslMarker
annotation class TextMessageDsl

/** Represent a simple message with a text to be sent or edit using the [sendMessage][TelegramService.sendMessage] method */
@TextMessageDsl
class TextMessage(override val service: TelegramService = TelegramApi.service) : EditableMessage, KeyboardCreator {

    override var keyboardMarkup: MessageKeyboard? = null

    /** Full text of message with entities */
    private var messageText: MessageText? = null

    /** Set true for apply null strings to message text */
    var printNulls: Boolean = false

    /** Simple text to send the message, optionally with [parseMode] */
    var text: String? = null

    /** [Parse mode][ParseMode] for a simple text */
    var parseMode: ParseMode? = null

    /** Disables link previews for links in this message */
    var disableWebPagePreview: Boolean? = null

    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * */
    override var disableNotification: Boolean? = null

    /** If the message is a reply, ID of the original message */
    override var replyToMessageId: Int? = null

    /** Pass True, if the message should be sent even if the specified replied-to message is not found */
    override var allowSendingWithoutReply: Boolean? = null

    /** Sets the text as a [MessageText] object */
    @TextMessageDsl
    fun textWithEntities(setText: MessageText.() -> Unit) {
        messageText = MessageText(printNulls)
        messageText!!.apply(setText)
    }

    /** Returns the raw text with safe length for sending the message, empty string if not set */
    private fun getMessageText() = messageText?.getSafeTextString() ?: text?.withSafeLength() ?: ""

    /** Returns the [parseMode] if entities were not used, null otherwise */
    private fun getParseModeIdNeed() = if (messageText != null) null else parseMode

    override fun send(chatId: String): Response<MessageResponse> {
        return service.sendMessage(
            chatId = chatId,
            text = getMessageText(),
            parseMode = getParseModeIdNeed(),
            entities = messageText?.getSafeEntitiesString(),
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute()
    }

    override fun edit(chatId: String?, messageId: Long?, inlineMessageId: String?) : Response<MessageResponse> {
        val msg = getMessageText()
        if (msg.isEmpty()) {
            return service.editMessageReplyMarkup(
                chatId = chatId,
                messageId = messageId,
                inlineMessageId = inlineMessageId,
                replyMarkup = keyboardMarkup?.toMarkup()
            ).execute()
        } else {
            return service.editMessageText(
                chatId = chatId,
                messageId = messageId,
                inlineMessageId = inlineMessageId,
                text = getMessageText(),
                parseMode = getParseModeIdNeed(),
                entities = messageText?.getSafeEntitiesString(),
                replyMarkup = keyboardMarkup?.toMarkup(),
                disableWebPagePreview = disableWebPagePreview
            ).execute()
        }
    }
}