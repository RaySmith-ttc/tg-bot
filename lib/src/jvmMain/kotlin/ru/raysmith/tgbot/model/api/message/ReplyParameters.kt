package ru.raysmith.tgbot.model.api.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.bot.ChatId

/** Describes reply parameters for the message that is being sent. */
@Serializable
data class ReplyParameters(

    /**
     * Identifier of the message that will be replied to in the current chat, or in the chat chat_id if it is specified
     * */
    @SerialName("message_id") val messageId: Int,

    /**
     * If the message to be replied to is from a different chat, unique identifier for the chat or username of the bot,
     * supergroup or channel in the format `@username`. Not supported for messages sent on behalf of a business account
     * and messages from channel direct messages chats.
     * */
    @get:JvmName("getChatIdField") // for getChatId method availability
    @SerialName("chat_id") val chatId: ChatId? = null,

    /**
     * Pass *True* if the message should be sent even if the specified message to be replied to is not found.
     * Always *False* for replies in another chat or forum topic.
     * Always *True* for messages sent on behalf of a business account.
     * */
    @SerialName("allow_sending_without_reply") val allowSendingWithoutReply: Boolean? = null,

    /**
     * Quoted part of the message to be replied to; 0-1024 characters after entities parsing. The quote must be an
     * exact substring of the message to be replied to, including *bold*, *italic*, *underline*, *strikethrough*,
     * *spoiler*, *custom_emoji*, and *date_time* entities.
     * The message will fail to send if the quote isn't found in the original message.
     * */
    @SerialName("quote") val quote: String? = null,

    /** Mode for parsing entities in the quote */
    @SerialName("quote_parse_mode") val quoteParseMode: ParseMode? = null,

    /** A list of special entities that appear in the quote. It can be specified instead of [quoteParseMode]. */
    @SerialName("quote_entities") val quoteEntities: List<MessageEntity>? = null,

    /** Position of the quote in the original message in UTF-16 code units */
    @SerialName("quote_position") val quotePosition: Int? = null,

    /** Identifier of the specific checklist task to be replied to */
    @SerialName("checklist_task_id") val checklistTaskId: Int? = null,

    /** Persistent identifier of the specific poll option to be replied to */
    @SerialName("poll_option_id") val pollOptionId: String? = null,
) : ChatIdHolder {
    override fun getChatId() = chatId
}
