package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.response.MessageEditResponse
import ru.raysmith.tgbot.model.network.message.response.MessageSendResponse

interface ChatID {
    var chatId: String?
}

interface ISender : ChatID {
    fun send(text: String): Response<MessageSendResponse> = send { this.text = text }
    fun send(message: TextMessage.() -> Unit): Response<MessageSendResponse> {
        return TextMessage().apply(message).send(chatId!!)
    }
    fun sendPhoto(message: PhotoMessage.() -> Unit): Response<MessageSendResponse> {
        return PhotoMessage().apply(message).send(chatId!!)
    }
}

interface IEditor : ChatID {
    var messageId: Long?
    var inlineMessageId: String?

    fun edit(text: String) = edit { this.text = text }
    fun edit(message: TextMessage.() -> Unit): Response<MessageEditResponse> {
        return TextMessage().apply(message).edit(chatId, messageId, inlineMessageId)
    }
}