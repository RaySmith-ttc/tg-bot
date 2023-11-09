package ru.raysmith.tgbot.model.bot.message

import io.ktor.client.*
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramService2
import ru.raysmith.tgbot.utils.noimpl

interface MessageWithReplyMarkup : EditableMessage, KeyboardCreator {
    companion object {
        internal fun instance(service: TelegramService2) = object : MessageWithReplyMarkup {
            override val client: HttpClient = service.client
            override var messageThreadId: Int? = null
            override var disableNotification: Boolean? = null
            override var replyToMessageId: Int? = null
            override var allowSendingWithoutReply: Boolean? = null
            override var protectContent: Boolean? = null
            override var keyboardMarkup: MessageKeyboard? = null
            override fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?) = noimpl()
            override fun send(chatId: ChatId) = noimpl()
        }
    }

    override fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message {
        return editMessageReplyMarkup(
            chatId = chatId,
            messageId = messageId,
            inlineMessageId = inlineMessageId,
            replyMarkup = keyboardMarkup?.toMarkup()
        )
    }
}

interface EditableMessage : IMessage<Message> {

    /** Edit message from [chat] with [messageId] or [inlineMessageId] */
    fun edit(chat: Chat?, messageId: Int?, inlineMessageId: String?): Message =
        edit(chat?.id, messageId, inlineMessageId)

    /** Edit message from chat with [chatId] and [messageId] or [inlineMessageId] */
    fun edit(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message

    fun editReplyMarkup(chatId: ChatId?, messageId: Int?, inlineMessageId: String?): Message
}