package ru.raysmith.tgbot.utils

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.InvoiceSender
import ru.raysmith.tgbot.core.handler.UnknownEventHandler
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageId
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramService

internal fun errorBody(): Nothing = throw NullPointerException("The method did not return a body")

/**
 * Creates a bot context and executes a [block] that can call API requests
 *
 * @param withChatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param service Telegram service with a bot token
 * */
inline fun botContext(withChatId: String? = null, service: TelegramService = TelegramApi.service, block: BotContext<UnknownEventHandler>.() -> Unit) {
    object : BotContext<UnknownEventHandler> {
        override val service: TelegramService = service
        override fun getChatId(): String? = withChatId
        override fun withBot(bot: Bot, block: BotContext<UnknownEventHandler>.() -> Any) {
            UnknownEventHandler(Update(-1)).apply {
                this.service = bot.service
                block()
            }
        }
    }.apply(block)
}

/**
 * A simple method for testing your bot's auth token. Requires no parameters.
 *
 * Returns basic information about the bot in form of a [User] object.
 * */
fun BotContext<*>.getMe() = service.getMe().execute().body()?.result ?: errorBody()

/**
 * Use this method to log out from the cloud Bot API server before launching the bot locally.
 * You **must** log out the bot before running it locally, otherwise there is no guarantee that the bot will
 * receive updates. After a successful call, you can immediately log in on a local server, but will not be able
 * to log in back to the cloud Bot API server for 10 minutes. Returns *True* on success. Requires no parameters.
 * */
fun BotContext<*>.logOut() = service.logOut().execute().body()?.result ?: errorBody()

/**
 * Use this method to close the bot instance before moving it from one local server to another.
 * You need to delete the webhook before calling this method to ensure that the bot isn't launched again after
 * server restart. The method will return error 429 in the first 10 minutes after the bot is launched.
 * Returns *True* on success. Requires no parameters.
 * */
fun BotContext<*>.close() = service.close().execute().body()?.result ?: errorBody()

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
fun BotContext<*>.sendMessage(
    text: String,
    parseMode: ParseMode? = null,
    entities: String? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: Int? = null,
    allowSendingWithoutReply: Boolean? = null,
    keyboardMarkup: KeyboardMarkup? = null,
    chatId: String = getChatIdOrThrow()
): Message {
    return service.sendMessage(
        chatId, text, parseMode, entities, disableWebPagePreview, disableNotification, protectContent,
        replyToMessageId, allowSendingWithoutReply, keyboardMarkup
    ).execute().body()?.result ?: errorBody()
}

fun BotContext<*>.editMessageText(
    chatId: String? = null,
    messageId: Int? = null,
    inlineMessageId: String? = null,
    text: String,
    parseMode: ParseMode? = null,
    entities: String? = null,
    disableWebPagePreview: Boolean? = null,
    replyMarkup: KeyboardMarkup? = null
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
fun BotContext<*>.forwardMessage(
    fromChatId: String,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    messageId: Int,
    chatId: String = getChatIdOrThrow()
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
fun BotContext<*>.copyMessage(
    fromChatId: String,
    messageId: Int,
    caption: String? = null,
    parseMode: ParseMode? = null,
    captionEntities: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: Int? = null,
    allowSendingWithoutReply: Boolean? = null,
    keyboardMarkup: KeyboardMarkup? = null,
    chatId: String = getChatIdOrThrow()
): MessageId {
    return service.copyMessage(
        chatId, fromChatId, messageId, caption, parseMode, captionEntities, disableNotification, protectContent,
        replyToMessageId, allowSendingWithoutReply, keyboardMarkup
    ).execute().body()?.result ?: errorBody()
}



fun BotContext<*>.getChat(id: String) = service.getChat(id).execute().body()?.result ?: errorBody()

fun BotContext<*>.deleteMessage(messageId: Int, chatId: String = getChatIdOrThrow()) =
    service.deleteMessage(chatId, messageId).execute().body()?.result ?: errorBody()

/**
 * Use this method to change the list of the bot's commands.
 *
 * @see <a href="https://core.telegram.org/bots#commands">commands</a> for more details about bot commands. Returns True on success
 *
 * @param commands list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
 * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * */
fun BotContext<*>.setMyCommands(commands: List<BotCommand>, scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
    return service.setMyCommands(TelegramApi.json.encodeToString(commands), scope, languageCode)
        .execute().body()?.result ?: errorBody()
}

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language.
 * Returns Array of [BotCommand] on success. If commands aren't set, an empty list is returned.
 *
 * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * */
fun BotContext<*>.getMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): List<BotCommand> {
    return service.getMyCommands(scope, languageCode).execute().body()?.result ?: errorBody()
}

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language.
 * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns True on success.
 *
 * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * */
fun BotContext<*>.deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
    return service.deleteMyCommands(scope, languageCode).execute().body()?.result ?: errorBody()
}

/**
 * Send invoice to chat with [chatId] using the given [buildAction]. On success, the sent [Message] is returned.
 *
 * @see [TelegramService.sendInvoice]
 * */
fun BotContext<*>.sendInvoice(chatId: String = this.getChatIdOrThrow(), buildAction: InvoiceSender.() -> Unit): Message {
    return InvoiceSender(service).apply(buildAction).send(chatId).result
}