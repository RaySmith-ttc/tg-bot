package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.group.AudioMediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.group.DocumentMediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.group.PhotoMediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.group.VideoMediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.media.*
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.utils.errorBody
import ru.raysmith.tgbot.utils.toChatId

// TODO add docs for methods
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

    fun sendVideo(chatId: Long, message: VideoMessage.() -> Unit): Message = sendVideo(chatId.toChatId(), message)
    fun sendVideo(chatId: ChatId = getChatIdOrThrow(), message: VideoMessage.() -> Unit): Message {
        return VideoMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendAnimation(chatId: Long, message: AnimationMessage.() -> Unit): Message = sendAnimation(chatId.toChatId(), message)
    fun sendAnimation(chatId: ChatId = getChatIdOrThrow(), message: AnimationMessage.() -> Unit): Message {
        return AnimationMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendVoice(chatId: Long, message: VoiceMessage.() -> Unit): Message = sendVoice(chatId.toChatId(), message)
    fun sendVoice(chatId: ChatId = getChatIdOrThrow(), message: VoiceMessage.() -> Unit): Message {
        return VoiceMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendVideoNote(chatId: Long, message: VideoNoteMessage.() -> Unit): Message = sendVideoNote(chatId.toChatId(), message)
    fun sendVideoNote(chatId: ChatId = getChatIdOrThrow(), message: VideoNoteMessage.() -> Unit): Message {
        return VideoNoteMessage(service, fileService).apply(message).send(chatId).result
    }

    fun sendLocation(chatId: Long, latitude: Double, longitude: Double, message: LocationMessage.() -> Unit): Message = sendLocation(latitude, longitude, chatId.toChatId(), message)
    fun sendLocation(latitude: Double, longitude: Double, chatId: ChatId = getChatIdOrThrow(), message: LocationMessage.() -> Unit): Message {
        return LocationMessage(latitude, longitude, service, fileService).apply(message).send(chatId).result
    }

    fun editMessageLiveLocation(chatId: Long, latitude: Double, longitude: Double, messageId: Int? = null, inlineMessageId: String? = null, message: LocationMessage.() -> Unit) = editMessageLiveLocation(latitude, longitude, chatId.toChatId(), messageId, inlineMessageId, message)
    fun editMessageLiveLocation(latitude: Double, longitude: Double, chatId: ChatId? = getChatIdOrThrow(), messageId: Int? = null, inlineMessageId: String? = null, message: LocationMessage.() -> Unit): LiveLocationResponse {
        return LocationMessage(latitude, longitude, service, fileService).apply(message).edit(chatId, messageId, inlineMessageId)
    }

    fun stopMessageLiveLocation(chatId: Long, messageId: Int? = null, inlineMessageId: String? = null, message: LocationMessage.() -> Unit): LiveLocationResponse = stopMessageLiveLocation(chatId.toChatId(), messageId, inlineMessageId, message)
    fun stopMessageLiveLocation(chatId: ChatId = getChatIdOrThrow(), messageId: Int? = null, inlineMessageId: String? = null, message: LocationMessage.() -> Unit): LiveLocationResponse {
        return LocationMessage(0.0, 0.0, service, fileService).apply(message).stop(chatId, messageId, inlineMessageId)
    }

    fun sendVenue(chatId: Long, latitude: Double, longitude: Double, title: String, address: String, message: VenueMessage.() -> Unit): Message = sendVenue(latitude, longitude, title, address, chatId.toChatId(), message)
    fun sendVenue(latitude: Double, longitude: Double, title: String, address: String, chatId: ChatId = getChatIdOrThrow(), message: VenueMessage.() -> Unit): Message {
        return VenueMessage(latitude, longitude, title, address, service, fileService).apply(message).send(chatId).result
    }

    fun sendContact(chatId: Long, phoneNumber: String, firstName: String, message: ContactMessage.() -> Unit): Message = sendContact(phoneNumber, firstName, chatId.toChatId(), message)
    fun sendContact(phoneNumber: String, firstName: String, chatId: ChatId = getChatIdOrThrow(), message: ContactMessage.() -> Unit): Message {
        return ContactMessage(phoneNumber, firstName, service, fileService).apply(message).send(chatId).result
    }

    fun sendPoll(chatId: Long, question: String, options: List<String>, message: PollMessage.() -> Unit): Message = sendPoll(question, options, chatId.toChatId(), message)
    fun sendPoll(question: String, options: List<String>, chatId: ChatId = getChatIdOrThrow(), message: PollMessage.() -> Unit): Message {
        return PollMessage(question, options, service, fileService).apply(message).send(chatId).result
    }

    fun sendDice(chatId: Long, emoji: String, message: DiceMessage.() -> Unit): Message = sendDice(emoji, chatId.toChatId(), message)
    fun sendDice(emoji: String, chatId: ChatId = getChatIdOrThrow(), message: DiceMessage.() -> Unit): Message {
        return DiceMessage(emoji, service, fileService).apply(message).send(chatId).result
    }

    fun sendChatAction(chatId: Long, action: ChatAction): Boolean = sendChatAction(action, chatId.toChatId())
    fun sendChatAction(action: ChatAction, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return service.sendChatAction(chatId, action).execute().body()?.result ?: errorBody()
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

    fun sendVideoMediaGroup(chatId: Long, message: VideoMediaGroupMessage.() -> Unit): List<Message> = sendVideoMediaGroup(chatId.toChatId(), message)
    fun sendVideoMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: VideoMediaGroupMessage.() -> Unit): List<Message> {
        return VideoMediaGroupMessage(service, fileService).apply(message).send(chatId).results
    }
}