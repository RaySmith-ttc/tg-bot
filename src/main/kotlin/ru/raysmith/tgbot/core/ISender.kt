package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

interface ChatID {
    var chatId: String?
}

interface ISender : ChatID {

    fun send(chatId: String = this.chatId!!, message: TextMessage.() -> Unit): Message {
        return TextMessage().apply(message).send(chatId)
            .body()?.result ?: throw NullPointerException("Send message has no response body")
    }

    fun sendPhoto(chatId: String = this.chatId!!, message: PhotoMessage.() -> Unit): Message {
        return PhotoMessage().apply(message).send(chatId)
            .body()?.result ?: throw NullPointerException("Send photo has no response body")
    }
}