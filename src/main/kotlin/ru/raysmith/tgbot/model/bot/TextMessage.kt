package ru.raysmith.tgbot.model.bot

import org.slf4j.LoggerFactory
import retrofit2.Response
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.MessageResponse
import ru.raysmith.tgbot.network.TelegramApi

class TextMessage : EditableMessage, KeyboardCreator() {
    companion object {
        private val logger = LoggerFactory.getLogger("text-message")
    }

    private var _text: MessageText? = null
    var text: String? = null
        set(value) {
            if (_text != null) logger.warn("Setting text in TextMessage after setting text with entities has no effect")
            field = value
        }
    var parseMode: ParseMode? = null
    var entities: List<MessageEntity>? = null
    var disableWebPagePreview: Boolean? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null

    /** Sets text as [MessageText] object */
    fun textWithEntities(setText: MessageText.() -> Unit) {
        _text = MessageText()
        _text!!.apply(setText)
    }
    private fun getMessageText() = _text?.getTextString() ?: text ?: ""

    override fun send(chatId: String): Response<MessageResponse> {
        return TelegramApi.service.sendMessage(
            chatId = chatId,
            text = getMessageText(),
            parseMode = parseMode,
            entities = _text?.getEntitiesString(),
            disableWebPagePreview = disableWebPagePreview,
            disableNotification = disableNotification,
            replyToMessageId = replyToMessageId,
            allowSendingWithoutReply = allowSendingWithoutReply,
            keyboardMarkup = keyboardMarkup?.toMarkup()
        ).execute()
    }

    override fun edit(chatId: String?, messageId: Long?, inlineMessageId: String?) : Response<MessageResponse> {
        return TelegramApi.service.editMessageText(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            text = getMessageText(),
            parseMode = parseMode,
            entities = _text?.getEntitiesString(),
            replyMarkup = keyboardMarkup?.toMarkup(),
            disableWebPagePreview = disableWebPagePreview
        ).execute()
    }
}