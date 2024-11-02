package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.ReplyParameters
import ru.raysmith.tgbot.network.API

interface IMessage<T> : API {
    companion object {
        const val MAX_TEXT_LENGTH = 4096
        const val MAX_CAPTION_LENGTH = 1024
        const val MAX_POLL_QUESTION_LENGTH = 300
        const val MAX_POLL_EXPLANATION_LENGTH = 200
        const val MAX_POLL_OPTION_LENGTH = 100
    }

    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * */
    var disableNotification: Boolean?

    /** Description of the message to reply to */
    var replyParameters: ReplyParameters?

    /** Protects the contents of the sent message from forwarding and saving */
    var protectContent: Boolean?

    /**
     * Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
     * per message. The relevant Stars will be withdrawn from the bot's balance
     * */
    var allowPaidBroadcast: Boolean?

    /** Sends message to the [chat] */
    suspend fun send(chat: Chat): T = send(chat.id)

    /** Sends message to the chat with [chatId] */
    suspend fun send(chatId: ChatId): T
}

interface BusinessMessage<T> : IMessage<T> {

    /** Unique identifier of the business connection on behalf of which the message will be sent */
    var businessConnectionId: String?
}

interface ExtendedMessage<T> : BusinessMessage<T> {

    /** Unique identifier for the target message thread (topic) of the forum; for forum supergroups only */
    var messageThreadId: Int?

    /** Unique identifier of the message effect to be added to the message; for private chats only */
    var messageEffectId: String?
}