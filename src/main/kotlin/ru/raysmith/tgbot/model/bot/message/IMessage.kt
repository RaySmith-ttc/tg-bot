package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.ReplyParameters
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

    /** Description of the message to reply to */
    var replyParameters: ReplyParameters?

    /** Protects the contents of the sent message from forwarding and saving */
    var protectContent: Boolean?

    /** Unique identifier of the business connection on behalf of which the message will be sent */
    var businessConnectionId: String?

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