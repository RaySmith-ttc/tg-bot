package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.message.MessageRes

interface IMessage<T : MessageRes> : ApiCaller {
    companion object {
        const val MAX_TEXT_LENGTH = 4096
        const val MAX_CAPTION_LENGTH = 1024
        const val MAX_POLL_EXPLANATION_LENGTH = 200
    }

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

    /** Send message to [chat] */
    fun send(chat: Chat): T = send(chat.id)

    /** Send message to chat with [chatId] */
    fun send(chatId: ChatId): T
}

