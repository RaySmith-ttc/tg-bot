package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

interface ChatID {
    var chatId: String?
}

interface ISender : ChatID {
    fun send(text: String) = send { this.text = text }

    fun send(message: TextMessage.() -> Unit): Message {
        return TextMessage().apply(message).send(chatId!!.toString())
            .body()?.result ?: throw NullPointerException("Send message has no response body")
    }

    // TODO return body
    fun sendPhoto(message: PhotoMessage.() -> Unit): Message {
        return PhotoMessage().apply(message).send(chatId!!.toString())
            .body()?.result ?: throw NullPointerException("Send photo has no response body")
    }
}

interface IEditor : ChatID {
    var messageId: Long?
    var inlineMessageId: String?

    fun edit(text: String) = edit { this.text = text }

    fun edit(message: TextMessage.() -> Unit): Message {
        return TextMessage().apply(message).edit(chatId.toString(), messageId, inlineMessageId)
            .body()?.result ?: throw NullPointerException("Edit message has no response body")
    }
}