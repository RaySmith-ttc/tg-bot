package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.group.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.media.*
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.network.TelegramService2
import ru.raysmith.tgbot.utils.toChatId

// TODO add docs for methods
/** Represent an object that can send messages */
interface ISender : ChatIdHolder, TelegramService2 {

    // TODO change agts to chatId, messageThreadId = Int? = null
    suspend fun send(chatId: Long, message: suspend TextMessage.() -> Unit): Message = send(chatId.toChatId(), message)
    suspend fun send(chatId: ChatId = getChatIdOrThrow(), message: suspend TextMessage.() -> Unit): Message {
        return TextMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendPhoto(chatId: Long, message: suspend PhotoMessage.() -> Unit): Message =
        sendPhoto(chatId.toChatId(), message)

    suspend fun sendPhoto(chatId: ChatId = getChatIdOrThrow(), message: suspend PhotoMessage.() -> Unit): Message {
        return PhotoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAudio(chatId: Long, message: suspend AudioMessage.() -> Unit): Message =
        sendAudio(chatId.toChatId(), message)

    suspend fun sendAudio(chatId: ChatId = getChatIdOrThrow(), message: suspend AudioMessage.() -> Unit): Message {
        return AudioMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendDocument(chatId: Long, message: suspend DocumentMessage.() -> Unit): Message =
        sendDocument(chatId.toChatId(), message)

    suspend fun sendDocument(chatId: ChatId = getChatIdOrThrow(), message: suspend DocumentMessage.() -> Unit): Message {
        return DocumentMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideo(chatId: Long, message: suspend VideoMessage.() -> Unit): Message =
        sendVideo(chatId.toChatId(), message)

    suspend fun sendVideo(chatId: ChatId = getChatIdOrThrow(), message: suspend VideoMessage.() -> Unit): Message {
        return VideoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAnimation(chatId: Long, message: suspend AnimationMessage.() -> Unit): Message =
        sendAnimation(chatId.toChatId(), message)

    suspend fun sendAnimation(chatId: ChatId = getChatIdOrThrow(), message: suspend AnimationMessage.() -> Unit): Message {
        return AnimationMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVoice(chatId: Long, message: suspend VoiceMessage.() -> Unit): Message =
        sendVoice(chatId.toChatId(), message)

    suspend fun sendVoice(chatId: ChatId = getChatIdOrThrow(), message: suspend VoiceMessage.() -> Unit): Message {
        return VoiceMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideoNote(chatId: Long, message: suspend VideoNoteMessage.() -> Unit): Message =
        sendVideoNote(chatId.toChatId(), message)

    suspend fun sendVideoNote(chatId: ChatId = getChatIdOrThrow(), message: suspend VideoNoteMessage.() -> Unit): Message {
        return VideoNoteMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendLocation(
        chatId: Long,
        latitude: Double,
        longitude: Double,
        message: suspend LocationMessage.() -> Unit
    ): Message = sendLocation(latitude, longitude, chatId.toChatId(), message)

    suspend fun sendLocation(
        latitude: Double,
        longitude: Double,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend LocationMessage.() -> Unit
    ): Message {
        return LocationMessage(latitude, longitude, client).apply { message() }.send(chatId)
    }

    suspend fun editMessageLiveLocation(
        chatId: Long,
        latitude: Double,
        longitude: Double,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: suspend LocationMessage.() -> Unit
    ) = editMessageLiveLocation(latitude, longitude, chatId.toChatId(), messageId, inlineMessageId, message)

    suspend fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        chatId: ChatId? = getChatIdOrThrow(),
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: suspend LocationMessage.() -> Unit
    ): LiveLocationResponse {
        return LocationMessage(latitude, longitude, client).apply { message() }
            .edit(chatId, messageId, inlineMessageId)
    }

    suspend fun stopMessageLiveLocation(
        chatId: Long,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: suspend LocationMessage.() -> Unit
    ): LiveLocationResponse = stopMessageLiveLocation(chatId.toChatId(), messageId, inlineMessageId, message)

    suspend fun stopMessageLiveLocation(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: suspend LocationMessage.() -> Unit
    ): LiveLocationResponse {
        return LocationMessage(0.0, 0.0, client).apply { message() }.stop(chatId, messageId, inlineMessageId)
    }

    suspend fun sendVenue(
        chatId: Long,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        message: suspend VenueMessage.() -> Unit
    ): Message = sendVenue(latitude, longitude, title, address, chatId.toChatId(), message)

    suspend fun sendVenue(
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend VenueMessage.() -> Unit
    ): Message {
        return VenueMessage(latitude, longitude, title, address, client).apply { message() }.send(chatId)
    }

    suspend fun sendContact(chatId: Long, phoneNumber: String, firstName: String, message: suspend ContactMessage.() -> Unit): Message =
        sendContact(phoneNumber, firstName, chatId.toChatId(), message)

    suspend fun sendContact(
        phoneNumber: String,
        firstName: String,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend ContactMessage.() -> Unit
    ): Message {
        return ContactMessage(phoneNumber, firstName, client).apply { message() }.send(chatId)
    }

    suspend fun sendPoll(chatId: Long, question: String, options: List<String>, message: suspend PollMessage.() -> Unit): Message =
        sendPoll(question, options, chatId.toChatId(), message)

    suspend fun sendPoll(
        question: String,
        options: List<String>,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend PollMessage.() -> Unit
    ): Message {
        return PollMessage(question, options, client).apply { message() }.send(chatId)
    }

    suspend fun sendDice(chatId: Long, emoji: String, message: suspend DiceMessage.() -> Unit): Message =
        sendDice(emoji, chatId.toChatId(), message)

    suspend fun sendDice(emoji: String, chatId: ChatId = getChatIdOrThrow(), message: suspend DiceMessage.() -> Unit): Message {
        return DiceMessage(emoji, client).apply { message() }.send(chatId)
    }

    suspend fun sendChatAction(chatId: Long, messageThreadId: Int? = null, action: ChatAction): Boolean =
        sendChatAction(action, messageThreadId, chatId.toChatId())

    suspend fun sendChatAction(action: ChatAction, messageThreadId: Int? = null, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return sendChatAction(chatId, action, messageThreadId)
    }

    suspend fun sendMediaGroup(chatId: Long, message: suspend MediaGroupMessage.() -> Unit): List<Message> =
        sendMediaGroup(chatId.toChatId(), message)

    suspend fun sendMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: suspend MediaGroupMessage.() -> Unit): List<Message> {
        return MediaGroupMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendSticker(chatId: Long, message: suspend StickerMessage.() -> Unit): Message = sendSticker(chatId.toChatId(), message)
    suspend fun sendSticker(chatId: ChatId = getChatIdOrThrow(), message: suspend StickerMessage.() -> Unit): Message {
        return StickerMessage(client).apply { message() }.send(chatId)
    }
}