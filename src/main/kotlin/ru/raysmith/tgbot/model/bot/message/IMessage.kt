package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.network.API

interface IMessage<T> : API {
    companion object {
        const val MAX_TEXT_LENGTH = 4096
        const val MAX_CAPTION_LENGTH = 1024
        const val MAX_POLL_EXPLANATION_LENGTH = 200
    }

    /** Unique identifier for the target message thread (topic) of the forum; for forum supergroups only */
    var messageThreadId: Int?

    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * */
    var disableNotification: Boolean?

    /** If the message is a reply, ID of the original message */
    var replyToMessageId: Int?

    /** Pass True, if the message should be sent even if the specified replied-to message is not found */
    var allowSendingWithoutReply: Boolean?

    /** Protects the contents of the sent message from forwarding and saving */
    var protectContent: Boolean?

    /**
     * Send message to [chat]
     *
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * */
    suspend fun send(chat: Chat, messageThreadId: Int? = null): T = send(chat.id, messageThreadId)

    /**
     * Send message to chat with [chatId]
     *
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * */
    suspend fun send(chatId: ChatId, messageThreadId: Int? = null): T
}