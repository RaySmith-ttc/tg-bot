package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.network.API

@MessageDsl
interface IMessage<T> : API {
    companion object {
        const val MAX_TEXT_LENGTH = 4096
        const val MAX_CAPTION_LENGTH = 1024
        const val MAX_POLL_QUESTION_LENGTH = 300
        const val MAX_POLL_EXPLANATION_LENGTH = 200
        const val MAX_POLL_DESCRIPTION_LENGTH = 1024
        const val MAX_POLL_OPTION_LENGTH = 100
        const val MAX_GIFT_TEXT_LENGTH = 255
        const val MAX_LENGTH_VERIFY_USER_DESCRIPTION = 70
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

    /** Identifier of the direct messages topic; required if the message is sent to a direct messages chat */
    var directMessagesTopicId: Int?

    /** Sends message to the [chat] */
    suspend fun send(chat: Chat): T = send(chat.id)

    // TODO should contains all fields: (?)
    //        inlineMessageId: String?,
    //        businessConnectionId: String?,
    //        directMessagesTopicId: Int?,
    /** Sends message to the chat with [chatId] */
    suspend fun send(chatId: ChatId): T
}

interface BusinessMessage {

    /** Unique identifier of the business connection on behalf of which the message will be sent */
    var businessConnectionId: String?
}

interface ThreadMessage {

    /** Unique identifier for the target message thread (topic) of a forum; for forum supergroups only */
    var messageThreadId: Int?
}

interface EffectableMessage {

    /** Unique identifier of the message effect to be added to the message; for private chats only */
    var messageEffectId: String?
}

interface SuggestablePostMessage {

    /**
     * An object containing the parameters of the suggested post to send; for direct messages chats only. If the message
     * is sent as a reply to another suggested post, then that suggested post is automatically declined.
     * */
    var suggestedPostParameters: SuggestedPostParameters?
}
