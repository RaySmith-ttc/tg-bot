package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.PhotoMessage
import ru.raysmith.tgbot.model.bot.TextMessage
import ru.raysmith.tgbot.model.network.message.Message

/** Represent an object that can send messages */
interface ISender : ChatIdHolder, ApiCaller {

    fun send(chatId: String = getChatIdOrThrow(), message: TextMessage.() -> Unit): Message {
        return TextMessage(service).apply(message).send(chatId).result
    }

    fun sendPhoto(chatId: String = getChatIdOrThrow(), message: PhotoMessage.() -> Unit): Message {
        return PhotoMessage().apply(message).send(chatId).result
    }

    fun sendMediaGroup(chatId: String = getChatIdOrThrow(), message: MediaGroupMessage.() -> Unit): List<Message> {
        return MediaGroupMessage().apply(message).send(chatId).results
    }
}