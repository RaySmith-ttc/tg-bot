package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.*
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.utils.toChatId

/** Represent an object that can send messages */
interface ISender : ChatIdHolder, ApiCaller {

    fun send(chatId: Long, message: TextMessage.() -> Unit): Message = send(chatId.toChatId(), message)
    fun send(chatId: ChatId = getChatIdOrThrow(), message: TextMessage.() -> Unit): Message {
        return TextMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendPhoto(chatId: Long, message: PhotoMessage.() -> Unit): Message = sendPhoto(chatId.toChatId(), message)
    fun sendPhoto(chatId: ChatId = getChatIdOrThrow(), message: PhotoMessage.() -> Unit): Message {
        return PhotoMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendAudio(chatId: Long, message: AudioMessage.() -> Unit): Message = sendAudio(chatId.toChatId(), message)
    fun sendAudio(chatId: ChatId = getChatIdOrThrow(), message: AudioMessage.() -> Unit): Message {
        return AudioMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendDocument(chatId: Long, message: DocumentMessage.() -> Unit): Message = sendDocument(chatId.toChatId(), message)
    fun sendDocument(chatId: ChatId = getChatIdOrThrow(), message: DocumentMessage.() -> Unit): Message {
        return DocumentMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendAudioMediaGroup(chatId: Long, message: AudioMediaGroupMessage.() -> Unit): List<Message> = sendAudioMediaGroup(chatId.toChatId(), message)
    fun sendAudioMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: AudioMediaGroupMessage.() -> Unit): List<Message> {
        return AudioMediaGroupMessage(service, fileService).apply(message).send(chatId).results
    }

    fun sendPhotoMediaGroup(chatId: Long, message: AudioMediaGroupMessage.() -> Unit): List<Message> = sendAudioMediaGroup(chatId.toChatId(), message)
    fun sendPhotoMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: PhotoMediaGroupMessage.() -> Unit): List<Message> {
        return PhotoMediaGroupMessage(service, fileService).apply(message).send(chatId).results
    }

    fun sendDocumentMediaGroup(chatId: Long, message: DocumentMediaGroupMessage.() -> Unit): List<Message> = sendDocumentMediaGroup(chatId.toChatId(), message)
    fun sendDocumentMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: DocumentMediaGroupMessage.() -> Unit): List<Message> {
        return DocumentMediaGroupMessage(service, fileService).apply(message).send(chatId).results
    }
}