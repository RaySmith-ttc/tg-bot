package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

interface IEditor : ChatID {
    var messageId: Long?
    var inlineMessageId: String?

    fun edit(
        chatId: String = this.chatId!!,
        messageId: Long? = this.messageId,
        inlineMessageId: String? = this.inlineMessageId,
        message: TextMessage.() -> Unit
    ): Message {
        return TextMessage().apply(message).edit(chatId, messageId, inlineMessageId)
            .body()?.result ?: throw NullPointerException("Edit message has no response body")
    }
}