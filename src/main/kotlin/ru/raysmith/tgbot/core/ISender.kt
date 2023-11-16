package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.*
import ru.raysmith.tgbot.model.bot.message.group.MediaGroupMessage
import ru.raysmith.tgbot.model.bot.message.media.*
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.network.TelegramService2

// TODO add docs for methods
/** Represent an object that can send messages */
interface ISender : ChatIdHolder, TelegramService2 {

    suspend fun send(chatId: ChatId = getChatIdOrThrow(), message: TextMessage.() -> Unit): Message {
        return TextMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendPhoto(chatId: ChatId = getChatIdOrThrow(), message: PhotoMessage.() -> Unit): Message {
        return PhotoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAudio(chatId: ChatId = getChatIdOrThrow(), message: AudioMessage.() -> Unit): Message {
        return AudioMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendDocument(chatId: ChatId = getChatIdOrThrow(), message: DocumentMessage.() -> Unit): Message {
        return DocumentMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideo(chatId: ChatId = getChatIdOrThrow(), message: VideoMessage.() -> Unit): Message {
        return VideoMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendAnimation(chatId: ChatId = getChatIdOrThrow(), message: AnimationMessage.() -> Unit): Message {
        return AnimationMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVoice(chatId: ChatId = getChatIdOrThrow(), message: VoiceMessage.() -> Unit): Message {
        return VoiceMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendVideoNote(chatId: ChatId = getChatIdOrThrow(), message: VideoNoteMessage.() -> Unit): Message {
        return VideoNoteMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendLocation(
        latitude: Double,
        longitude: Double,
        chatId: ChatId = getChatIdOrThrow(),
        message: LocationMessage.() -> Unit
    ): Message {
        return LocationMessage(latitude, longitude, client).apply { message() }.send(chatId)
    }

    suspend fun editMessageLiveLocation(
        latitude: Double,
        longitude: Double,
        chatId: ChatId? = getChatIdOrThrow(),
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: LocationMessage.() -> Unit
    ): LiveLocationResponse {
        return LocationMessage(latitude, longitude, client).apply { message() }
            .edit(chatId, messageId, inlineMessageId)
    }

    suspend fun stopMessageLiveLocation(
        chatId: ChatId = getChatIdOrThrow(),
        messageId: Int? = null,
        inlineMessageId: String? = null,
        message: LocationMessage.() -> Unit
    ): LiveLocationResponse {
        return LocationMessage(0.0, 0.0, client).apply { message() }.stop(chatId, messageId, inlineMessageId)
    }

    suspend fun sendVenue(
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        chatId: ChatId = getChatIdOrThrow(),
        message: VenueMessage.() -> Unit
    ): Message {
        return VenueMessage(latitude, longitude, title, address, client).apply { message() }.send(chatId)
    }

    suspend fun sendContact(
        phoneNumber: String,
        firstName: String,
        chatId: ChatId = getChatIdOrThrow(),
        message: ContactMessage.() -> Unit
    ): Message {
        return ContactMessage(phoneNumber, firstName, client).apply { message() }.send(chatId)
    }

    suspend fun sendPoll(
        question: String,
        options: List<String>,
        chatId: ChatId = getChatIdOrThrow(),
        message: PollMessage.() -> Unit
    ): Message {
        return PollMessage(question, options, client).apply { message() }.send(chatId)
    }

    suspend fun sendDice(emoji: String, chatId: ChatId = getChatIdOrThrow(), message: DiceMessage.() -> Unit): Message {
        return DiceMessage(emoji, client).apply { message() }.send(chatId)
    }


    suspend fun sendChatAction(action: ChatAction, messageThreadId: Int? = null, chatId: ChatId = getChatIdOrThrow()): Boolean {
        return sendChatAction(chatId, action, messageThreadId)
    }

    suspend fun sendMediaGroup(chatId: ChatId = getChatIdOrThrow(), message: MediaGroupMessage.() -> Unit): List<Message> {
        return MediaGroupMessage(client).apply { message() }.send(chatId)
    }

    suspend fun sendSticker(chatId: ChatId = getChatIdOrThrow(), message: StickerMessage.() -> Unit): Message {
        return StickerMessage(client).apply { message() }.send(chatId)
    }
}