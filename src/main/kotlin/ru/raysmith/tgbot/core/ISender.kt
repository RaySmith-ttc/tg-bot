package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramService

/** Represent an object that can send messages */
interface ISender : ChatIdHolder, ApiCaller {

    override val service: TelegramService

    fun send(chatId: String = getChatIdOrThrow(), message: TextMessage.() -> Unit): Message {
        return (TextMessage(service).apply(message).send(chatId).body()?.result
            ?: throw NullPointerException("Send message has no response body"))
    }

    fun sendPhoto(chatId: String = getChatIdOrThrow(), message: PhotoMessage.() -> Unit): Message {
        return (PhotoMessage().apply(message).send(chatId).body()?.result
            ?: throw NullPointerException("Send photo has no response body"))
    }

    fun sendMediaGroup(chatId: String = getChatIdOrThrow(), message: MediaGroupMessage.() -> Unit): List<Message> {
        return (MediaGroupMessage().apply(message).send(chatId).body()?.results
            ?: throw NullPointerException("Send media group has no response body"))
    }
}