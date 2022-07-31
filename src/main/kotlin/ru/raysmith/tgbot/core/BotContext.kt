package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageId
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Allows to change a bot for the [handler][T] */
interface BotContext<T : EventHandler> : ChatIdHolder, ApiCaller {

    /** Uses the [bot] token to make requests to telegram from [block]. */
    fun withBot(bot: Bot, block: BotContext<T>.() -> Any)

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text.
     * @param entities List of special entities that appear in message text, which can be specified instead of *parse_mode*
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param keyboardMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * */
    fun sendMessage(
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return service.sendMessage(
            chatId, text, parseMode, entities, disableWebPagePreview, disableNotification, protectContent,
            replyToMessageId, allowSendingWithoutReply, keyboardMarkup
        ).execute().body()?.result ?: errorBody()
    }

    fun editMessageText(
        messageId: Int? = null,
        inlineMessageId: String? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return service.editMessageText(
            chatId, messageId, inlineMessageId, text, parseMode, entities, disableWebPagePreview, replyMarkup
        ).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent [Message] is returned.
     * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     *
     *  */
    fun forwardMessage(
        fromChatId: ChatId,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageId: Int,
        chatId: ChatId = getChatIdOrThrow()
    ): Message {
        return service.forwardMessage(chatId, fromChatId, disableNotification, protectContent, messageId)
            .execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
     * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the
     * original message. Returns the [MessageId] of the sent message on success.
     *
     * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
     * @param parseMode [ParseMode] for parsing entities in the message caption.
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of *parse_mode*
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param keyboardMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * */
// TODO tests
    fun copyMessage(
        fromChatId: ChatId,
        messageId: Int,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
        chatId: ChatId = getChatIdOrThrow()
    ): MessageId {
        return service.copyMessage(
            chatId, fromChatId, messageId, caption, parseMode, captionEntities, disableNotification, protectContent,
            replyToMessageId, allowSendingWithoutReply, keyboardMarkup
        ).execute().body()?.result ?: errorBody()
    }

    fun deleteMessage(messageId: Int, chatId: ChatId = getChatIdOrThrow()) =
        service.deleteMessage(chatId, messageId).execute().body()?.result ?: errorBody()

    /**
     * Send invoice to chat with [chatId] using the given [buildAction]. On success, the sent [Message] is returned.
     *
     * @see [TelegramService.sendInvoice]
     * */
    fun sendInvoice(chatId: ChatId = this.getChatIdOrThrow(), buildAction: InvoiceSender.() -> Unit): Message {
        return InvoiceSender(service, fileService).apply(buildAction).send(chatId).result
    }
}