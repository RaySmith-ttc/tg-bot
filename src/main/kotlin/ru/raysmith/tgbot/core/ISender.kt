package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.group.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.media.*
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.network.API

// TODO add docs for methods
/** Represent an object that can send messages */
interface ISender : ChatIdHolder, API {

    suspend fun send(chatId: ChatId = getChatIdOrThrow(), message: suspend TextMessage.() -> Unit): Message {
        return TextMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendPhoto(chatId: ChatId = getChatIdOrThrow(), message: suspend PhotoMessage.() -> Unit): Message {
        return PhotoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAudio(chatId: ChatId = getChatIdOrThrow(), message: suspend AudioMessage.() -> Unit): Message {
        return AudioMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendDocument(chatId: ChatId = getChatIdOrThrow(), message: suspend DocumentMessage.() -> Unit): Message {
        return DocumentMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideo(chatId: ChatId = getChatIdOrThrow(), message: suspend VideoMessage.() -> Unit): Message {
        return VideoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAnimation(chatId: ChatId = getChatIdOrThrow(), message: suspend AnimationMessage.() -> Unit): Message {
        return AnimationMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVoice(chatId: ChatId = getChatIdOrThrow(), message: suspend VoiceMessage.() -> Unit): Message {
        return VoiceMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideoNote(chatId: ChatId = getChatIdOrThrow(), message: suspend VideoNoteMessage.() -> Unit): Message {
        return VideoNoteMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendLocation(
        latitude: Double,
        longitude: Double,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend LocationMessage.() -> Unit
    ): Message {
        return LocationMessage(latitude, longitude, client).apply { message() }.send(chatId)
    }

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
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: suspend LocationMessage.() -> Unit
    ): LiveLocationResponse {
        return LocationMessage(0.0, 0.0, client).apply { message() }.stop(chatId, messageId, inlineMessageId)
    }

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

    suspend fun sendContact(
        phoneNumber: String,
        firstName: String,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend ContactMessage.() -> Unit
    ): Message {
        return ContactMessage(phoneNumber, firstName, client).apply { message() }.send(chatId)
    }

    suspend fun sendPoll(
        question: String,
        options: List<String>,
        chatId: ChatId = getChatIdOrThrow(),
        message: suspend PollMessage.() -> Unit
    ): Message {
        return PollMessage(question, options, client).apply { message() }.send(chatId)
    }

    suspend fun sendDice(emoji: String, chatId: ChatId = getChatIdOrThrow(), message: suspend DiceMessage.() -> Unit): Message {
        return DiceMessage(emoji, client).apply { message() }.send(chatId)
    }


    suspend fun sendChatAction(action: ChatAction, messageThreadId: Int? = null, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return sendChatAction(chatId, action, messageThreadId)
    }

    suspend fun sendMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: suspend MediaGroupMessage.() -> Unit): List<Message> {
        return MediaGroupMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendSticker(chatId: ChatId = getChatIdOrThrow(), message: suspend StickerMessage.() -> Unit): Message {
        return StickerMessage(client).apply { message() }.send(chatId)
    }
}