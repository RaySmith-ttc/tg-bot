@file:Suppress("MemberVisibilityCanBePrivate")

package ru.raysmith.tgbot.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import io.ktor.utils.io.streams.*
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.bot.BotDescription
import ru.raysmith.tgbot.model.bot.BotName
import ru.raysmith.tgbot.model.bot.BotShortDescription
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.model.network.*
import ru.raysmith.tgbot.model.network.chat.*
import ru.raysmith.tgbot.model.network.chat.forum.ForumTopic
import ru.raysmith.tgbot.model.network.chat.forum.IconColor
import ru.raysmith.tgbot.model.network.chat.member.ChatMember
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.file.File
import ru.raysmith.tgbot.model.network.inline.SentWebAppMessage
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultsButton
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.menubutton.MenuButton
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.MessageId
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.PollType
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.model.network.response.NetworkResponse
import ru.raysmith.tgbot.model.network.sticker.*
import ru.raysmith.tgbot.model.network.updates.Update
import java.io.InputStream
import java.time.ZonedDateTime
import kotlin.time.Duration

interface API {
    val client: HttpClient

    private suspend inline fun <reified T> request(crossinline block: suspend () -> HttpResponse): T {
        val response = block()

        if (response.status.isSuccess()) {
            return when {
                T::class == String::class -> response.bodyAsText() as T
                T::class == InputStream::class -> response.bodyAsChannel().toInputStream() as T
                T::class == LiveLocationResponse::class -> response.body<T>()
                T::class == NetworkResponse::class -> response.body<T>()
                else -> response.body<NetworkResponse<T>>().result
            }
        } else if (response.status == HttpStatusCode.Unauthorized) {
            throw BotException("Bot is unauthorized with token")
        } else {
            val error = response.body<Error>()
            throw TelegramApiException(error, response.request)
        }
    }

    /**
     * Use this method to receive incoming updates using long polling
     * ([wiki](https://en.wikipedia.org/wiki/Push_technology#Long_polling)).
     * */
    suspend fun getUpdates(
        offset: Int? = null,
        limit: Int? = null,
        timeout: Int? = null,
        allowedUpdates: String? = null,
    ) = request<List<Update>> {
        client.post("getUpdates") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("timeout", timeout)
            parameter("allowed_updates", allowedUpdates)
        }
    }

    /**
     * Use this method to specify a URL and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL,
     * containing a JSON-serialized [Update]. In case of an unsuccessful request, we will give up after a
     * reasonable amount of attempts. Returns *True* on Success.
     *
     * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter
     * secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token”
     * with the secret token as content.
     *
     * **Notes:**
     * 1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up.
     * 2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter. Please upload as InputFile, sending a String will not work.
     * 3. Ports currently supported for webhooks: 443, 80, 88, 8443.
     *
     * If you're having any trouble setting up webhooks, please check out this amazing guide to webhooks.
     *
     * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
     * @param certificate Upload your public key certificate so that the root certificate in use can be checked.
     * See our [self-signed](https://core.telegram.org/bots/self-signed) guide for details.
     * @param ipAddress The fixed IP address which will be used to send webhook requests
     * instead of the IP address resolved through DNS
     * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update
     * delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot's server, and higher
     * values to increase your bot's throughput.
     * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive.
     * See [Update] for a complete list of available update types. Specify an empty list to receive all update
     * types except chat_member (default). If not specified, the previous setting will be used.
     *
     * Please note that this parameter doesn't affect updates created before the call to the setWebhook,
     * so unwanted updates may be received for a short period of time.
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     * @param secretToken A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook
     * request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     * The header is useful to ensure that the request comes from a webhook set by you.
     * */
    suspend fun setWebhook(
        url: String,
        certificate: InputFile? = null,
        ipAddress: String? = null,
        maxConnections: Int? = null,
        allowedUpdates: String? = null,
        dropPendingUpdates: Boolean? = null,
        secretToken: String? = null,
    ) = request<Boolean> {
        client.post("setWebhook") {
            parameter("url", url)
            parameter("ip_address", ipAddress)
            parameter("max_connections", maxConnections)
            parameter("allowed_updates", allowedUpdates)
            parameter("drop_pending_updates", dropPendingUpdates)
            parameter("secret_token", secretToken)
            setMultiPartFormDataBody(
                "certificate" to certificate,
            )
        }
    }

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns *True* on success.
     *
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     * */
    suspend fun deleteWebhook(
        dropPendingUpdates: Boolean? = null,
    ) = request<Boolean> {
        client.post("deleteWebhook") {
            parameter("drop_pending_updates", dropPendingUpdates)
        }
    }

    /**
     * Use this method to get current webhook status. Requires no parameters.
     * On success, returns a [WebhookInfo] object. If the bot is using getUpdates,
     * will return an object with the url field empty.
     * */
    suspend fun getWebhookInfo() = request<WebhookInfo> {
        client.post("getWebhookInfo")
    }

    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     *
     * Returns basic information about the bot in form of a [User] object.
     * */
    suspend fun getMe() = request<User> {
        client.post("getMe")
    }

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally.
     * You **must** log out the bot before running it locally, otherwise there is no guarantee that the bot will
     * receive updates. After a successful call, you can immediately log in on a local server, but will not be able
     * to log in back to the cloud Bot API server for 10 minutes. Returns *True* on success. Requires no parameters.
     * */
    suspend fun logOut() = request<Boolean> {
        client.post("logOut")
    }

    /**
     * Use this method to close the bot instance before moving it from one local server to another.
     * You need to delete the webhook before calling this method to ensure that the bot isn't launched again after
     * server restart. The method will return error 429 in the first 10 minutes after the bot is launched.
     * Returns *True* on success. Requires no parameters.
     * */
    suspend fun close() = request<Boolean> {
        client.post("close")
    }

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text.
     * @param entities List of special entities that appear in message text, which can be specified instead of *[parseMode]*
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param keyboardMarkup Additional interface options. Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun sendMessage(
        chatId: ChatId,
        messageThreadId: Int? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("text", text)
            parameter("parse_mode", parseMode)
            parameter("entities", entities)
            parameter("disable_web_page_preview", disableWebPagePreview)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    /**
     * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent [Message] is returned.
     * @param fromChatId Unique identifier for the chat where the original message was sent
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param chatId Unique identifier for the target chat or username of the target channel
     *
     * */
    suspend fun forwardMessage(
        chatId: ChatId,
        messageThreadId: Int? = null,
        fromChatId: ChatId,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageId: Int,
    ) = request<Message> {
        client.post("forwardMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("from_chat_id", fromChatId)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("message_id", messageId)
        }
    }

    /**
     * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
     * The method is analogous to the method [forwardMessage], but the copied message doesn't have a link to the
     * original message. Returns the [MessageId] of the sent message on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param fromChatId Unique identifier for the chat where the original message was sent
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param caption New caption for media, 0-1024 characters after entities parsing.
     * If not specified, the original caption is kept
     * @param parseMode [ParseMode] for parsing entities in the message caption.
     * @param captionEntities List of special entities that appear in message text,
     * which can be specified instead of *[parseMode]*
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified
     * replied-to message is not found
     * @param replyMarkup Additional interface options. Object for an
     * [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     * instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun copyMessage(
        chatId: ChatId,
        messageThreadId: Int? = null,
        fromChatId: ChatId,
        messageId: Int,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<MessageId> {
        client.post("copyMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("from_chat_id", fromChatId)
            parameter("message_id", messageId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", replyMarkup)
        }
    }

    /**
     * Use this method to send photos
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param photo Photo to send. Pass an [InputFile.FileIdOrUrl] with file id to send a photo that exists on the
     * Telegram servers (recommended), pass an [InputFile.FileIdOrUrl] with HTTP URL for Telegram to get a photo from
     * the Internet, or upload a new photo using [InputFile.File]. The photo must be at most 10 MB in size.
     * The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20.
     * [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * @param caption Photo caption (may also be used when resending photos by [InputFile.FileIdOrUrl]),
     * 0-1024 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the photo caption.
     * @param captionEntities List of special entities that appear in message text,
     * which can be specified instead of *[parseMode]*
     * @param hasSpoiler Pass *True* if the photo needs to be covered with a spoiler animation
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified
     * replied-to message is not found
     * @param replyMarkup Additional interface options. Object for an
     * [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     * instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun sendPhoto(
        chatId: ChatId,
        messageThreadId: Int? = null,
        photo: InputFile,
        caption: String? = null,
        captionEntities: String? = null,
        hasSpoiler: Boolean? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendPhoto") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("has_spoiler", hasSpoiler)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", replyMarkup)
            setMultiPartFormDataBody(
                "photo" to photo,
            )
        }
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player.
     * Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned.
     * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of the forum;
     * for forum supergroups only
     * @param audio Audio file to send
     * @param caption Audio caption, 0-1024 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the audio caption.
     * @param captionEntities List of special entities that appear in message text,
     * which can be specified instead of *[parseMode]*
     * @param duration Duration of the audio in seconds
     * @param performer Performer
     * @param title Track name
     * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported
     * server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height
     * should not exceed 320. Ignored if the file is not uploaded using [InputFile.File].
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified
     * replied-to message is not found
     * @param replyMarkup Additional interface options. Object for an
     * [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     * instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun sendAudio(
        chatId: ChatId,
        messageThreadId: Int? = null,
        audio: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        thumbnail: NotReusableInputFile? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendAudio") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("duration", duration)
            parameter("performer", performer)
            parameter("title", title)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", replyMarkup)
            setMultiPartFormDataBody(
                "audio" to audio,
                "thumbnail" to thumbnail as InputFile
            )
        }
    }

    suspend fun sendDocument(
        chatId: ChatId,
        messageThreadId: Int? = null,
        document: InputFile,
        thumbnail: NotReusableInputFile?,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        disableContentTypeDetection: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendDocument") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("disable_content_type_detection", disableContentTypeDetection)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "document" to document,
                "thumbnail" to thumbnail as InputFile,
            )
        }
    }

    suspend fun sendVideo(
        chatId: ChatId,
        messageThreadId: Int? = null,
        video: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        hasSpoiler: Boolean? = null,
        supportsStreaming: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVideo") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("duration", duration)
            parameter("width", width)
            parameter("height", height)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("has_spoiler", hasSpoiler)
            parameter("supports_streaming", supportsStreaming)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "video" to video
            )
        }
    }

    suspend fun sendAnimation(
        chatId: ChatId,
        messageThreadId: Int? = null,
        animation: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumbnail: String? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        hasSpoiler: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendAnimation") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("duration", duration)
            parameter("width", width)
            parameter("height", height)
            parameter("thumbnail", thumbnail)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("has_spoiler", hasSpoiler)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "animation" to animation
            )
        }
    }

    suspend fun sendVoice(
        chatId: ChatId,
        messageThreadId: Int? = null,
        voice: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        duration: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVoice") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("duration", duration)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "voice" to voice
            )
        }
    }

    suspend fun sendVideoNote(
        chatId: ChatId,
        messageThreadId: Int? = null,
        videoNote: InputFile,
        duration: Int? = null,
        length: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVideoNote") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("duration", duration)
            parameter("length", length)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "video_note" to videoNote
            )
        }
    }

    /**
     * Use this method to send a group of photos, videos, documents or audios as an album.
     * Documents and audio filescan be only grouped in an album with messages of the same type.
     * On success, an array of Messages that were sent is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * @param media A JSON-serialized array describing messages to be sent, must include 2-10 items
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass True, if the message should be sent even if the specified replied-to message is not found
     * */
    suspend fun sendMediaGroup(
        chatId: ChatId,
        messageThreadId: Int? = null,
        media: List<InputMediaGroup>,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        inputFiles: List<InputFile>? = null // TODO     docs
    ) = request<List<Message>> {
        client.post("sendMediaGroup") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("media", media)
            parameter("disable_notification", disableNotification)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            if (inputFiles != null) {
                var lastInputFilesIndex = 0
                setMultiPartFormDataBody(
                    *buildList {
                        media.map { inputMedia ->
                            println(inputMedia.media)
                            when(inputMedia) {
                                is InputMediaGroupWithThumbnail -> {
                                    add(inputMedia.media.drop(MediaRequest.attachProtocolLength) to inputFiles[lastInputFilesIndex++])
                                    if (inputMedia.thumbnail != null) {
                                        add(inputMedia.thumbnail!!.drop(MediaRequest.attachProtocolLength) to inputFiles[lastInputFilesIndex++])
                                    } else {}
                                }
                                else -> add(inputMedia.media.drop(MediaRequest.attachProtocolLength) to inputFiles[lastInputFilesIndex++])
                            }
                        }
                    }.toTypedArray()
                )
            }
        }
    }

    suspend fun sendLocation(
        chatId: ChatId,
        messageThreadId: Int? = null,
        latitude: Double,
        longitude: Double,
        horizontalAccuracy: Double? = null,
        livePeriod: Int? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendLocation") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("horizontal_accuracy", horizontalAccuracy)
            parameter("live_period", livePeriod)
            parameter("heading", heading)
            parameter("proximity_alert_radius", proximityAlertRadius)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun sendVenue(
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        latitude: Double,
        longitude: Double,
        title: String,
        address: String,
        foursquareId: String? = null,
        foursquareType: String? = null,
        googlePlaceId: String? = null,
        googlePlaceType: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVenue") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("title", title)
            parameter("address", address)
            parameter("foursquare_id", foursquareId)
            parameter("foursquare_type", foursquareType)
            parameter("google_place_id", googlePlaceId)
            parameter("google_place_type", googlePlaceType)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun sendContact(
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        phoneNumber: String,
        firstName: String,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendContact") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("phone_number", phoneNumber)
            parameter("first_name", firstName)
            parameter("last_name", lastName)
            parameter("vcard", vcard)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun sendPoll(
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        question: String,
        options: String,
        isAnonymous: Boolean? = null,
        type: PollType? = null,
        allowsMultipleAnswers: Boolean? = null,
        correctOptionId: Int? = null,
        explanation: String? = null,
        explanationParseMode: String? = null,
        explanationEntities: String? = null,
        openPeriod: Int? = null,
        closeDate: Int? = null,
        isClosed: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendPoll") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("question", question)
            parameter("options", options)
            parameter("is_anonymous", isAnonymous)
            parameter("type", type)
            parameter("allows_multiple_answers", allowsMultipleAnswers)
            parameter("correct_option_id", correctOptionId)
            parameter("explanation", explanation)
            parameter("explanation_parse_mode", explanationParseMode)
            parameter("explanation_entities", explanationEntities)
            parameter("open_period", openPeriod)
            parameter("close_date", closeDate)
            parameter("is_closed", isClosed)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun sendDice(
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        emoji: String,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendDice") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("emoji", emoji)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun sendChatAction(
        chatId: ChatId,
        action: ChatAction,
        messageThreadId: Int? = null,
    ) = request<Boolean> {
        client.post("sendChatAction") {
            parameter("chat_id", chatId)
            parameter("action", action)
            parameter("message_thread_id", messageThreadId)
        }
    }

    /**
     * Use this method to get a list of profile pictures for a user. Returns a [UserProfilePhotos] object.
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * */
    suspend fun getUserProfilePhotos(
        userId: ChatId.ID,
        offset: Int? = null,
        limit: Int? = null,
    ) = request<UserProfilePhotos> {
        client.post("getUserProfilePhotos") {
            parameter("user_id", userId)
            parameter("offset", offset)
            parameter("limit", limit)
        }
    }

    /**
     * Use this method to get basic information about a file and prepare it for downloading.
     * For the moment, bots can download files of up to 20MB in size. On success, a [File] object is returned.
     * The file can then be downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`,
     * where `<file_path>` is taken from the response. It is guaranteed that the link will be valid for at least 1 hour.
     * When the link expires, a new one can be requested by calling [getFile] again.
     *
     * **Note:** This function may not preserve the original file name and MIME type.
     * You should save the file's MIME type and name (if available) when the File object is received.
     *
     * @param id File identifier to get information about
     * */
    suspend fun getFile(
        id: String,
    ) = request<File> {
        client.post("getFile") {
            parameter("file_id", id)
        }
    }

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,
     * the user will not be able to return to the chat on their own using invite links, etc., unless [unbanned][unbanChatMember] first.
     * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
     * Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are
     * considered to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * */
    suspend fun banChatMember(
        chatId: ChatId,
        userId: ChatId.ID,
        untilDate: Until? = null,
        revokeMessages: Boolean? = null,
    ) = request<Boolean> {
        client.post("banChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("until_date", untilDate)
            parameter("revoke_messages", revokeMessages)
        }
    }

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the
     * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for
     * this to work. By default, this method guarantees that after the call the user is not a member of the chat,
     * but will be able to join it. So if the user is a member of the chat they will also be **removed** from the chat.
     * If you don't want this, use the parameter _[onlyIfBanned]_. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     * */
    suspend fun unbanChatMember(
        chatId: ChatId,
        userId: ChatId.ID,
        onlyIfBanned: Boolean? = null,
    ) = request<Boolean> {
        client.post("unbanChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("only_if_banned", onlyIfBanned)
        }
    }

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this
     * to work and must have the appropriate administrator rights.
     * Pass *True* for all permissions to lift restrictions from a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param permissions New user permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews]
     * permissions will imply the [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios],
     * [ChatPermissions.canSendDocuments], [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos],
     * [ChatPermissions.canSendVideoNotes], and [ChatPermissions.canSendVoiceNotes] permissions;
     * the [ChatPermissions.canSendPolls] permission will imply the [ChatPermissions.canSendMessages] permission.
     * @param untilDate Date when restrictions will be lifted for the user. If user is restricted for more than
     * 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun restrictChatMember(
        chatId: ChatId,
        userId: ChatId.ID,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
        untilDate: Until? = null,
    ) = request<Boolean> {
        client.post("restrictChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("permissions", permissions)
            parameter("use_independent_chat_permissions", useIndependentChatPermissions)
            parameter("until_date", untilDate)
        }
    }

    /**
     * Use this method to promote or demote a user in a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Pass _False_ for all boolean parameters to demote a user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param administratorRights New administrator rights
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun promoteChatMember( // TODO docs
        chatId: ChatId,
        userId: ChatId.ID,
        isAnonymous: Boolean? = null,
        canManageChat: Boolean? = null,
        canPostMessages: Boolean? = null,
        canEditMessages: Boolean? = null,
        canDeleteMessages: Boolean? = null,
        canPostStories: Boolean? = null,
        canEditStories: Boolean? = null,
        canDeleteStories: Boolean? = null,
        canManageVideoChats: Boolean? = null,
        canRestrictMembers: Boolean? = null,
        canPromoteMembers: Boolean? = null,
        canChangeInfo: Boolean? = null,
        canInviteUsers: Boolean? = null,
        canPinMessages: Boolean? = null,
        canManageTopics: Boolean? = null,
    ) = request<Boolean> {
        client.post("promoteChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("is_anonymous", isAnonymous)
            parameter("can_manage_chat", canManageChat)
            parameter("can_post_messages", canPostMessages)
            parameter("can_edit_messages", canEditMessages)
            parameter("can_delete_messages", canDeleteMessages)
            parameter("can_post_stories", canPostStories)
            parameter("can_edit_stories", canEditStories)
            parameter("can_delete_stories", canDeleteStories)
            parameter("can_manage_video_chats", canManageVideoChats)
            parameter("can_restrict_members", canRestrictMembers)
            parameter("can_promote_members", canPromoteMembers)
            parameter("can_change_info", canChangeInfo)
            parameter("can_invite_users", canInviteUsers)
            parameter("can_pin_messages", canPinMessages)
            parameter("can_manage_topics", canManageTopics)
        }
    }

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot.
     * Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param customTitle Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: ChatId.ID,
        customTitle: String,
    ) = request<Boolean> {
        client.post("setChatAdministratorCustomTitle") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("custom_title", customTitle)
        }
    }

    /**
     * Use this method to ban a channel chat in a supergroup or a channel.
     * Until the chat is unbanned[unbanChatSenderChat], the owner of the banned chat won't be able to send messages on
     * behalf of **any of their channels**. The bot must be an administrator in the supergroup or channel for this to
     * work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param senderChatId    Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun banChatSenderChat(
        chatId: ChatId,
        senderChatId: ChatId.ID,
    ) = request<Boolean> {
        client.post("banChatSenderChat") {
            parameter("chat_id", chatId)
            parameter("sender_chat_id", senderChatId)
        }
    }

    /**
     * Use this method to unban a previously banned channel chat in a supergroup or channel.
     * The bot must be an administrator for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param senderChatId Unique identifier of the target sender chat
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unbanChatSenderChat(
        chatId: ChatId,
        senderChatId: ChatId.ID,
    ) = request<Boolean> {
        client.post("unbanChatSenderChat") {
            parameter("chat_id", chatId)
            parameter("sender_chat_id", senderChatId)
        }
    }

    /**
     * Use this method to set default chat permissions for all members.
     * The bot must be an administrator in the group or a supergroup for this to work and must have the
     * _can_restrict_members_ administrator rights. Returns *True* on success.
     *
     * @param permissions New default chat permissions
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently.
     * Otherwise, the [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews]
     * permissions will imply the [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios],
     * [ChatPermissions.canSendDocuments], [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos],
     * [ChatPermissions.canSendVideoNotes], and [ChatPermissions.canSendVoiceNotes] permissions;
     * the [ChatPermissions.canSendPolls] permission will imply the [ChatPermissions.canSendMessages] permission.
     * */
    suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
    ) = request<Boolean> {
        client.post("setChatPermissions") {
            parameter("chat_id", chatId)
            parameter("permissions", permissions)
            parameter("use_independent_chat_permissions", useIndependentChatPermissions)
        }
    }

    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated primary link
     * is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate
     * administrator rights. Returns the new invite link as String on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     *
     * > Note: Each administrator in a chat generates their own invite links. Bots can't use invite links generated
     * by other administrators. If you want your bot to work with invite links, it will need to generate its own link
     * using [exportChatInviteLink] or by calling the [getChat] method. If your bot needs to generate a new primary
     * invite link replacing its previous one, use [exportChatInviteLink] again.
     * */
    suspend fun exportChatInviteLink(
        chatId: ChatId,
    ) = request<NetworkResponse<String>> {
        client.post("exportChatInviteLink") {
            parameter("chat_id", chatId)
        }
    }.result

    /**
     * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat
     * for this to work and must have the appropriate administrator rights. The link can be revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink] object.
     *
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining
     * the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     * administrators. If *True*, _member_limit_ can't be specified
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun createChatInviteLink(
        chatId: ChatId,
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
    ) = request<ChatInviteLink> {
        client.post("createChatInviteLink") {
            parameter("chat_id", chatId)
            parameter("name", name)
            parameter("expire_date", expireDate?.toEpochSecond())
            parameter("member_limit", memberLimit)
            parameter("creates_join_request", createsJoinRequest)
        }
    }

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the
     * chat for this to work and must have the appropriate administrator rights.
     * Returns the edited invite link as a [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining
     * the chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     * administrators. If *True*, _member_limit_ can't be specified
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun editChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String? = null,
        expireDate: ZonedDateTime? = null,
        memberLimit: Int? = null,
        createsJoinRequest: Boolean? = null,
    ) = request<ChatInviteLink> {
        client.post("editChatInviteLink") {
            parameter("chat_id", chatId)
            parameter("invite_link", inviteLink)
            parameter("name", name)
            parameter("expire_date", expireDate?.toEpochSecond())
            parameter("member_limit", memberLimit)
            parameter("creates_join_request", createsJoinRequest)
        }
    }

    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
     * automatically generated. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns the revoked invite link as [ChatInviteLink] object.
     *
     * @param inviteLink The invite link to revoke
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun revokeChatInviteLink(
        chatId: ChatId,
        inviteLink: String,
    ) = request<ChatInviteLink> {
        client.post("revokeChatInviteLink") {
            parameter("chat_id", chatId)
            parameter("invite_link", inviteLink)
        }
    }

    /**
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work
     * and must have the _can_invite_users_ administrator right. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun approveChatJoinRequest(
        chatId: ChatId,
        userId: ChatId.ID,
    ) = request<Boolean> {
        client.post("approveChatJoinRequest") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work
     * and must have the _can_invite_users_ administrator right. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun declineChatJoinRequest(
        chatId: ChatId,
        userId: ChatId.ID,
    ) = request<Boolean> {
        client.post("declineChatJoinRequest") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param photo New chat photo
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatPhoto(
        chatId: ChatId,
        photo: NotReusableInputFile,
    ) = request<Boolean> {
        client.post("setChatPhoto") {
            parameter("chat_id", chatId)
            setMultiPartFormDataBody(
                "photo" to photo as InputFile,
            )
        }
    }

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun deleteChatPhoto(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("deleteChatPhoto") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to change the title of a chat. Titles can't be changed for private chats.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param title New chat title, 1-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatTitle(
        chatId: ChatId,
        title: String,
    ) = request<Boolean> {
        client.post("setChatTitle") {
            parameter("chat_id", chatId)
            parameter("title", title)
        }
    }

    /**
     * Use this method to change the description of a group, a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Returns *True* on success.
     *
     * @param description New chat description, 0-255 characters
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatDescription(
        chatId: ChatId,
        description: String?,
    ) = request<Boolean> {
        client.post("setChatDescription") {
            parameter("chat_id", chatId)
            parameter("description", description)
        }
    }

    /**
     * Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat,
     * the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator
     * right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns *True* on success.
     *
     * @param messageId Identifier of a message to pin
     * @param disableNotification Pass True, if it is not necessary to send a notification to all chat members
     * about the new pinned message. Notifications are always disabled in channels and private chats.
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun pinChatMessage(
        chatId: ChatId,
        messageId: Int,
        disableNotification: Boolean? = null,
    ) = request<Boolean> {
        client.post("pinChatMessage") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("disable_notification", disableNotification)
        }
    }

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a
     * private chat, the bot must be an administrator in the chat for this to work and must have the
     * 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel.
     * Returns *True* on success.
     *
     * @param messageId Identifier of a message to unpin.
     * If not specified, the most recent pinned message (by sending date) will be unpinned.
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unpinChatMessage(
        chatId: ChatId,
        messageId: Int? = null,
    ) = request<Boolean> {
        client.post("unpinChatMessage") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
        }
    }

    /**
     * Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat,
     * the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages'
     * administrator right in a supergroup or 'can_edit_messages' administrator right in a channel.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun unpinAllChatMessages(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("unpinAllChatMessages") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method for your bot to leave a group, supergroup or channel. Returns *True* on success.
     *
     * @param chatId identifier for the target chat or username of the target channel
     * */
    suspend fun leaveChat(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("leaveChat") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get up to date information about the chat (current name of the user for one-on-one
     * conversations, current username of a user, group or channel, etc.).
     * Returns a [Chat] object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun getChat(
        chatId: ChatId
    ) = request<Chat> {
        client.post("getChat") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get a list of administrators in a chat, which aren't bots.
     * Returns an Array of [ChatMember] objects.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun getChatAdministrators(
        chatId: ChatId,
    ) = request<List<ChatMember>> {
        client.post("getChatAdministrators") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get the number of members in a chat. Returns _Int_ on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun getChatMemberCount(
        chatId: ChatId,
    ) = request<Int> {
        client.post("getChatMemberCount") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get information about a member of a chat. Returns a [ChatMember] object on success.
     *
     * @param userId Unique identifier of the target user
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun getChatMember(
        chatId: ChatId,
        userId: ChatId.ID,
    ) = request<ChatMember> {
        client.post("getChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights.
     * Use the field [canSetStickerSet][Chat.canSetStickerSet] optionally returned in [getChat] requests to check
     * if the bot can use this method. Returns *True* on success.
     *
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String,
    ) = request<Boolean> {
        client.post("setChatStickerSet") {
            parameter("chat_id", chatId)
            parameter("sticker_set_name", stickerSetName)
        }
    }

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights.
     * Use the field [*canSetStickerSet*][Chat.canSetStickerSet] optionally returned in [getChat] requests to check
     * if the bot can use this method. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * */
    suspend fun deleteChatStickerSet(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("deleteChatStickerSet") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
     * */
    suspend fun getForumTopicIconStickers(

    ) = request<List<Sticker>> {
        client.post("getForumTopicIconStickers") {
        }
    }

    /**
     * Use this method to create a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * Returns information about the created topic as a [ForumTopic] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name Topic name, 1-128 characters
     * @param iconColor Color of the topic icon
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon.
     * Use [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * */
    suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: IconColor? = null,
        iconCustomEmojiId: String? = null,
    ) = request<ForumTopic> {
        client.post("createForumTopic") {
            parameter("chat_id", chatId)
            parameter("name", name)
            parameter("icon_color", iconColor)
            parameter("icon_custom_emoji_id", iconCustomEmojiId)
        }
    }

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param name New topic name, 0-128 characters. If not specified or empty,
     * the current name of the topic will be kept
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon.
     * Use [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * ass an empty string to remove the icon. If not specified, the current icon will be kept
     * */
    suspend fun editForumTopic(
        chatId: ChatId,
        messageThreadId: Int,
        name: String? = null,
        iconCustomEmojiId: String? = null,
    ) = request<Boolean> {
        client.post("editForumTopic") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("name", name)
            parameter("icon_custom_emoji_id", iconCustomEmojiId)
        }
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights,
     * unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun closeForumTopic(
        chatId: ChatId,
        messageThreadId: Int,
    ) = request<Boolean> {
        client.post("closeForumTopic") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
        }
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights,
     * unless it is the creator of the topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun reopenForumTopic(
        chatId: ChatId,
        messageThreadId: Int,
    ) = request<Boolean> {
        client.post("reopenForumTopic") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
        }
    }

    /**
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canDeleteMessages*][ChatAdministratorRights.canDeleteMessages] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun deleteForumTopic(
        chatId: ChatId,
        messageThreadId: Int,
    ) = request<Boolean> {
        client.post("deleteForumTopic") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
        }
    }

    /**
     * Use this method to clear the list of pinned messages in a forum topic.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canPinMessages*][ChatAdministratorRights.canPinMessages] administrator right in the supergroup.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * */
    suspend fun unpinAllForumTopicMessages(
        chatId: ChatId,
        messageThreadId: Int,
    ) = request<Boolean> {
        client.post("unpinAllForumTopicMessages") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
        }
    }

    /**
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name New topic name, 1-128 characters
     * */
    suspend fun editGeneralForumTopic(
        chatId: ChatId,
        name: String,
    ) = request<Boolean> {
        client.post("editGeneralForumTopic") {
            parameter("chat_id", chatId)
            parameter("name", name)
        }
    }

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun closeGeneralForumTopic(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("closeGeneralForumTopic") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically unhidden if it was hidden. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun reopenGeneralForumTopic(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("reopenGeneralForumTopic") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun hideGeneralForumTopic(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("hideGeneralForumTopic") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to unhide the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun unhideGeneralForumTopic(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("unhideGeneralForumTopic") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to clear the list of pinned messages in a General forum topic.
     * The bot must be an administrator in the chat for this to work and must have the
     * [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator rights.
     * The topic will be automatically closed if it was open. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun unpinAllGeneralForumTopicMessages(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("unpinAllGeneralForumTopicMessages") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to send answers to callback queries sent from [inline keyboards](https://core.telegram.org/bots/features#inline-keyboards). The answer will be displayed
     * to the user as a notification at the top of the chat screen or as an alert. On success, *True* is returned.
     * */
    suspend fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null,
        cacheTime: Int? = null,
    ) = request<Boolean> {
        client.post("answerCallbackQuery") {
            parameter("callback_query_id", callbackQueryId)
            parameter("text", text)
            parameter("show_alert", showAlert)
            parameter("url", url)
            parameter("cache_time", cacheTime)
        }
    }

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @see <a href="https://core.telegram.org/bots#commands">commands</a> for more details about bot commands.
     * Returns *True* on success
     *
     * @param commands list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all
     * users fromthe given scope, for whose language there are no dedicated commands
     * */
    suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("setMyCommands") {
            parameter("commands", commands)
            parameter("scope", scope)
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language.
     * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns *True* on success.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    suspend fun deleteMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("deleteMyCommands") {
            parameter("scope", scope)
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user language.
     * Returns Array of [BotCommand] on success. If commands aren't set, an empty list is returned.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    suspend fun getMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<List<BotCommand>> {
        client.post("getMyCommands") {
            parameter("scope", scope)
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to change the bot's name. Returns *True* on success.
     *
     * @param name New bot name; 0-64 characters.
     * Pass an empty string to remove the dedicated name for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all
     * users for whose language there is no dedicated name.
     * */
    suspend fun setMyName(
        name: String?,
        languageCode: String?,
    ) = request<Boolean> {
        client.post("setMyName") {
            parameter("name", name)
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to get the current bot name for the given user language. Returns [BotName] on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyName(
        languageCode: String?,
    ) = request<BotName> {
        client.post("getMyName") {
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty.
     * Returns *True* on success.
     *
     * @param description New bot description; 0-512 characters.
     * Pass an empty string to remove the dedicated description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code.
     * If empty, the description will be applied to all users for whose language there is no dedicated description.
     * */
    suspend fun setMyDescription(
        description: String?,
        languageCode: String?,
    ) = request<Boolean> {
        client.post("setMyDescription") {
            parameter("description", description)
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to get the current bot description for the given user language.
     * Returns [BotDescription] on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyDescription(
        languageCode: String?,
    ) = request<BotDescription> {
        client.post("getMyDescription") {
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent
     * together with the link when users share the bot. Returns *True* on success.
     *
     * @param shortDescription New short description for the bot; 0-120 characters.
     * Pass an empty string to remove the dedicated short description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty,
     * the short description will be applied to all users for whose language there is no dedicated short description.
     * */
    suspend fun setMyShortDescription(
        shortDescription: String?,
        languageCode: String?,
    ) = request<Boolean> {
        client.post("setMyShortDescription") {
            parameter("short_description", shortDescription)
            parameter("language_code", languageCode)
        }
    }

    /**
     * UUse this method to get the current bot short description for the given user language.
     * Returns [BotShortDescription] on success.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyShortDescription(
        languageCode: String?,
    ) = request<BotShortDescription> {
        client.post("getMyShortDescription") {
            parameter("language_code", languageCode)
        }
    }

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param chatId Unique identifier for the target private chat.
     * If not specified, default bot's menu button will be changed
     * @param menuButton [MenuButton] object for the bot's new menu button. Defaults to [MenuButtonDefault]
     * */
    suspend fun setChatMenuButton(
        chatId: ChatId? = null,
        menuButton: MenuButton? = null,
    ) = request<Boolean> {
        client.post("setChatMenuButton") {
            parameter("chat_id", chatId)
            parameter("menu_button", menuButton)
        }
    }

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param chatId Unique identifier for the target private chat.
     * If not specified, default bot's menu button will be returned
     * */
    suspend fun getChatMenuButton(
        chatId: ChatId? = null,
    ) = request<MenuButton> {
        client.post("getChatMenuButton") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to change the default administrator rights requested by the bot when it's added as an
     * administrator to groups or channels. These rights will be suggested to users, but they are are free to
     * modify the list before adding the bot. Returns *True* on success.
     *
     * @param rights [ChatAdministratorRights] object describing new default administrator rights.
     * If not specified, the default administrator rights will be cleared.
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     * */
    suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights? = null,
        forChannels: Boolean? = null,
    ) = request<Boolean> {
        client.post("setMyDefaultAdministratorRights") {
            parameter("rights", rights)
            parameter("for_channels", forChannels)
        }
    }

    /**
     * Use this method to get the current default administrator rights of the bot.
     * Returns ChatAdministratorRights on success.
     *
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be returned.
     * */
    suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean? = null,
    ) = request<ChatAdministratorRights> {
        client.post("getMyDefaultAdministratorRights") {
            parameter("for_channels", forChannels)
        }
    }

    /**
     * Use this method to edit text and game messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise True is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param text New text of the message, 1-4096 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text
     * @param entities A JSON-serialized list of special entities that appear in message text,
     * which can be specified instead of parseMode
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageText(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: String? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageText") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("text", text)
            parameter("parse_mode", parseMode)
            parameter("entities", entities)
            parameter("disable_web_page_preview", disableWebPagePreview)
            parameter("reply_markup", replyMarkup)
        }
    }

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise True is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param caption New caption of the message, 0-1024 characters after entities parsing
     * @param parseMode [ParseMode] for parsing entities in the message text
     * @param captionEntities List of special entities that appear in the caption,
     * which can be specified instead of [parseMode]
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageCaption(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        caption: String,
        parseMode: ParseMode? = null,
        captionEntities: String? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageCaption") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("caption", caption)
            parameter("parse_mode", parseMode)
            parameter("caption_entities", captionEntities)
            parameter("reply_markup", replyMarkup)
        }
    }

    /**
     * Use this method to edit animation, audio, document, photo, or video messages.
     * If a message is part of a message album, then it can be edited only to an audio for audio albums,
     * only to a document for document albums and to a photo or a video otherwise.
     * When an inline message is edited, a new file can't be uploaded; use a previously uploaded file
     * via its file_id or specify a URL. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise *True* is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param media New media content of the message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageMedia(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        media: InputMedia,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageMedia") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("media", media)
            parameter("reply_markup", replyMarkup)
        }
    }

    suspend fun editMessageLiveLocation(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        latitude: Double,
        longitude: Double,
        horizontalAccuracy: Double? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<LiveLocationResponse> {
        client.post("editMessageLiveLocation") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("horizontal_accuracy", horizontalAccuracy)
            parameter("heading", heading)
            parameter("proximity_alert_radius", proximityAlertRadius)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    suspend fun stopMessageLiveLocation(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<LiveLocationResponse> {
        client.post("stopMessageLiveLocation") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("reply_markup", keyboardMarkup)
        }
    }

    /**
     * Use this method to edit only the reply markup of messages. On success,
     * if the edited message is not an inline message, the edited [Message] is returned, otherwise *True* is returned.
     *
     * @param chatId Required if [inlineMessageId] is not specified.
     * Unique identifier for the target chat or username of the target channel
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageReplyMarkup(
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        replyMarkup: KeyboardMarkup? = null
    ) = request<Message> {
        client.post("editMessageReplyMarkup") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)
            parameter("reply_markup", replyMarkup)
        }
    }

    // TODO [alt] @param replyMarkup [MessageInlineKeyboard] builder for a new message inline keyboard.
    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll] is returned
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageId Identifier of the original message with the poll
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun stopPoll(
        chatId: ChatId,
        messageId: Int,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Poll> {
        client.post("stopPoll") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("reply_markup", replyMarkup)
        }
    }

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     * - A message can only be deleted if it was sent less than 48 hours ago.
     * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.
     * - Bots can delete incoming messages in private chats.
     * - Bots granted can_post_messages permissions can delete outgoing messages in channels.
     * - If the bot is an administrator of a group, it can delete any message there.
     * - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
     *
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageId Identifier of the message to delete
     * */
    suspend fun deleteMessage(
        chatId: ChatId,
        messageId: Int,
    ) = request<Boolean> {
        client.post("deleteMessage") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
        }
    }

    suspend fun sendSticker(
        chatId: ChatId,
        messageThreadId: Int? = null,
        sticker: InputFile,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        replyToMessageId: Int? = null,
        allowSendingWithoutReply: Boolean? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendSticker") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("emoji", emoji)
            parameter("disable_notification", disableNotification)
            parameter("protect_content", protectContent)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", keyboardMarkup)
            setMultiPartFormDataBody(
                "sticker" to sticker
            )
        }
    }

    /**
     * Use this method to get a sticker set. On success, a [StickerSet] object is returned.
     *
     * @param name Name of the sticker set
     * */
    suspend fun getStickerSet(
        name: String,
    ) = request<StickerSet> {
        client.post("getStickerSet") {
            parameter("name", name)
        }
    }

    /**
     * Use this method to get information about custom emoji stickers by their identifiers.
     * Returns an Array of [Sticker] objects.
     *
     * @param customEmojiIds List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     * */
    suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>,
    ) = request<List<Sticker>> {
        client.post("getCustomEmojiStickers") {
            parameter("custom_emoji_ids", customEmojiIds)
        }
    }

    /**
     * Use this method to upload a .PNG file with a sticker for later use in *[createNewStickerSet]* and
     * *[addStickerToSet]* methods (can be used multiple times). Returns the uploaded File on success.
     *
     * @param userId User identifier of sticker file owner
     * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format.
     * See [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements.
     * @param stickerFormat Format of the sticker
     * */
    suspend fun uploadStickerFile(
        userId: ChatId.ID,
        sticker: InputFile,
        stickerFormat: StickerFormat,
    ) = request<File> {
        client.post("uploadStickerFile") {
            parameter("user_id", userId)
            parameter("sticker_format", stickerFormat)
            setMultiPartFormDataBody(
                "sticker" to sticker
            )
        }
    }

    // TODO
    /**
     * Use this method to create a new sticker set owned by a user.
     * The bot will be able to edit the sticker set thus created. Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * You can use [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for
     * bot in context
     * @param title Sticker set title, 1-64 characters
     * @param block Sticker set builder
     * */
//    suspend fun createNewStickerSet(
//        userId: ChatId.ID,
//        name: String,
//        title: String,
//        stickerFormat: StickerFormat,
//        block: CreateNewStickerInStickerSet.() -> Unit,
//    ) = request<Boolean> {
//        val set = CreateNewStickerInStickerSet(userId, name, title, stickerFormat).apply(block)
//        client.post("createNewStickerSet") {
//            parameter("user_id", userId)
//            parameter("name", name)
//            parameter("title", title)
//            parameter("stickers", set.getAddedStickers())
//            parameter("sticker_format", stickerFormat)
//            parameter("sticker_type", set.stickerType)
//            parameter("needs_repainting", set.needsRepainting)
//            setMultiPartFormDataBody(
//                "mediaPart1" to mediaPart1,
//                "mediaPart2" to mediaPart2,
//                "mediaPart3" to mediaPart3,
//                "mediaPart4" to mediaPart4,
//                "mediaPart5" to mediaPart5,
//                "mediaPart6" to mediaPart6,
//                "mediaPart7" to mediaPart7,
//                "mediaPart8" to mediaPart8,
//                "mediaPart9" to mediaPart9,
//                "mediaPart10" to mediaPart10,
//                "mediaPart11" to mediaPart11,
//                "mediaPart12" to mediaPart12,
//                "mediaPart13" to mediaPart13,
//                "mediaPart14" to mediaPart14,
//                "mediaPart15" to mediaPart15,
//                "mediaPart16" to mediaPart16,
//                "mediaPart17" to mediaPart17,
//                "mediaPart18" to mediaPart18,
//                "mediaPart19" to mediaPart19,
//                "mediaPart20" to mediaPart20,
//                "mediaPart21" to mediaPart21,
//                "mediaPart22" to mediaPart22,
//                "mediaPart23" to mediaPart23,
//                "mediaPart24" to mediaPart24,
//                "mediaPart25" to mediaPart25,
//                "mediaPart26" to mediaPart26,
//                "mediaPart27" to mediaPart27,
//                "mediaPart28" to mediaPart28,
//                "mediaPart29" to mediaPart29,
//                "mediaPart30" to mediaPart30,
//                "mediaPart31" to mediaPart31,
//                "mediaPart32" to mediaPart32,
//                "mediaPart33" to mediaPart33,
//                "mediaPart34" to mediaPart34,
//                "mediaPart35" to mediaPart35,
//                "mediaPart36" to mediaPart36,
//                "mediaPart37" to mediaPart37,
//                "mediaPart38" to mediaPart38,
//                "mediaPart39" to mediaPart39,
//                "mediaPart40" to mediaPart40,
//                "mediaPart41" to mediaPart41,
//                "mediaPart42" to mediaPart42,
//                "mediaPart43" to mediaPart43,
//                "mediaPart44" to mediaPart44,
//                "mediaPart45" to mediaPart45,
//                "mediaPart46" to mediaPart46,
//                "mediaPart47" to mediaPart47,
//                "mediaPart48" to mediaPart48,
//                "mediaPart49" to mediaPart49,
//                "mediaPart50" to mediaPart50,
//            )
//        }
//    }

    /**
     * Use this method to add a new sticker to a set created by the bot.
     * You must use exactly one of the fields png_sticker, tgs_sticker, or webm_sticker.
     * Animated stickers can be added to animated sticker sets and only to them.
     * Animated sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers.
     * Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * You can use [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for
     * bot in context
     * @param sticker Information about the added sticker.
     * If exactly the same sticker had already been added to the set, then the set isn't changed.
     * */
    suspend fun addStickerToSet(
        userId: ChatId.ID,
        name: String,
        sticker: InputSticker,
    ) = request<Boolean> {
        client.post("addStickerToSet") {
            parameter("user_id", userId)
            parameter("name", name)
            parameter("sticker", sticker)
        }
    }

    /**
     * Use this method to move a sticker in a set created by the bot to a specific position. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param position New sticker position in the set, zero-based
     * */
    suspend fun setStickerPositionInSet(
        sticker: String,
        position: Int,
    ) = request<Boolean> {
        client.post("setStickerPositionInSet") {
            parameter("sticker", sticker)
            parameter("position", position)
        }
    }

    /**
     * Use this method to delete a sticker from a set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * */
    suspend fun deleteStickerFromSet(
        sticker: String,
    ) = request<Boolean> {
        client.post("deleteStickerFromSet") {
            parameter("sticker", sticker)
        }
    }

    /**
     * Use this method to change the list of emoji assigned to a regular or custom emoji sticker.
     * The sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param emojiList 1-20 emoji associated with the sticker
     * */
    suspend fun setStickerEmojiList(
        sticker: String,
        emojiList: List<String>,
    ) = request<Boolean> {
        client.post("setStickerEmojiList") {
            parameter("sticker", sticker)
            parameter("emoji_list", emojiList)
        }
    }

    /**
     * Use this method to change search keywords assigned to a regular or custom emoji sticker.
     * The sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param keywords 0-20 search keywords for the sticker with total length of up to 64 characters
     * */
    suspend fun setStickerKeywords(
        sticker: String,
        keywords: List<String>,
    ) = request<Boolean> {
        client.post("setStickerKeywords") {
            parameter("sticker", sticker)
            parameter("keywords", keywords)
        }
    }

    /**
     * Use this method to change the [mask position][MaskPosition] of a mask sticker.
     * The sticker must belong to a sticker set that was created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param maskPosition position where the mask should be placed on faces. Omit the parameter to remove the mask position.
     * */
    suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?,
    ) = request<Boolean> {
        client.post("setStickerMaskPosition") {
            parameter("sticker", sticker)
            parameter("mask_position", maskPosition)
        }
    }

    /**
     * Use this method to set the title of a created sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param title Sticker set title, 1-64 characters
     * */
    suspend fun setStickerSetTitle(
        name: String,
        title: String,
    ) = request<Boolean> {
        client.post("setStickerSetTitle") {
            parameter("name", name)
            parameter("title", title)
        }
    }

    /**
     * Use this method to set the thumbnail of a regular or mask sticker set.
     * The format of the thumbnail file must match the format of the stickers in the set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param userId User identifier of the sticker set owner
     * @param thumbnail A **PNG** image with the thumbnail, must be up to 128 kilobytes in size and have width and height
     * exactly 100px, or a **TGS** animation with the thumbnail up to 32 kilobytes in size;
     * see [Animation Requirements](https://core.telegram.org/stickers#animated-sticker-requirements)
     * for animated sticker technical requirements, or a **WEBM** video with the thumbnail up to 32 kilobytes in size;
     * see [Video Requirements](https://core.telegram.org/stickers#video-sticker-requirements) for video sticker
     * technical requirements. Pass a *file_id* as a String to send a file that already exists on the Telegram servers,
     * pass an HTTP URL as a String for Telegram to get a file from the Internet,
     * or upload a new one. [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files).
     * Animated sticker set thumbnails can't be uploaded via HTTP URL.
     * */
    suspend fun setStickerSetThumbnail(
        name: String,
        userId: ChatId.ID,
        thumbnail: InputFile,
    ) = request<Boolean> {
        client.post("setStickerSetThumbnail") {
            parameter("name", name)
            parameter("user_id", userId)
            setMultiPartFormDataBody(
                "thumbnail" to thumbnail,
            )
        }
    }

    /**
     * Use this method to set the thumbnail of a custom emoji sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
     * */
    suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String?,
    ) = request<Boolean> {
        client.post("setCustomEmojiStickerSetThumbnail") {
            parameter("name", name)
            parameter("custom_emoji_id", customEmojiId)
        }
    }

    /**
     * Use this method to delete a sticker set that was created by the bot. Returns *True* on success.
     *
     * @param name Sticker set name
     * */
    suspend fun deleteStickerSet(
        name: String,
    ) = request<Boolean> {
        client.post("deleteStickerSet") {
            parameter("name", name)
        }
    }

    /**
     * Use this method to send answers to an inline query. On success, *True* is returned.
     * No more than 50 results per query are allowed.
     *
     * @param inlineQueryId Unique identifier for the answered query
     * @param results List of results for the inline query
     * @param cacheTime The maximum amount of time that the result of the inline query may be cached on the server.
     * Defaults to 300 seconds.
     * @param isPersonal Pass *True* if results may be cached on the server side only for the user that sent the query.
     * By default, results may be returned to any user who sends the same query.
     * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive
     * more results. Pass an empty string if there are no more results or if you don't support pagination.
     * Offset length can't exceed 64 bytes.
     * @param button Button to be shown above inline query results
     * */
    suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Duration?,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        button: InlineQueryResultsButton? = null,
    ) = request<Boolean> {
        client.post("answerInlineQuery") {
            parameter("inline_query_id", inlineQueryId)
            parameter("results", results)
            parameter("cache_time", cacheTime)
            parameter("is_personal", isPersonal)
            parameter("next_offset", nextOffset)
            parameter("button", button)
        }
    }

    /**
     * Use this method to set the result of an interaction with a [Web App](https://core.telegram.org/bots/webapps)
     * and send a corresponding message on behalf of the user to the chat from which the query originated.
     * On success, a [SentWebAppMessage] object is returned.
     *
     * @param webAppQueryId Unique identifier for the query to be answered
     * @param result Object describing the message to be sent
     * */
    suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult,
    ) = request<SentWebAppMessage> {
        client.post("answerWebAppQuery") {
            parameter("web_app_query_id", webAppQueryId)
            parameter("result", result)
        }
    }

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather)
     * @param currency Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount,
     * delivery cost, delivery tax, bonus, etc.)
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency
     * (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * Defaults to 0
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * @param startParameter Unique deep-linking parameter. If left empty, *forwarded copies* of the sent message will
     * have a *Pay* button, allowing multiple users to pay directly from the forwarded message, using the same invoice.
     * If non-empty, forwarded copies of the sent message will have a URL button with a deep link to the bot
     * (instead of a *Pay* button), with the value used as the start parameter
     * @param providerData A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing
     * image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order
     * @param needEmail Pass *True*, if you require the user's email address to complete the order
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to provider
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to provider
     * @param isFlexible Pass *True*, if the final price depends on the shipping method
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to
     * message is not found
     * @param replyMarkup Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     * If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
     * */
    suspend fun sendInvoice(
        chatId: ChatId,
        messageThreadId: Int? = null,
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: String,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: String? = null,
        startParameter: String? = null,
        providerData: String? = null,
        photoUrl: String? = null,
        photoSize: Int? = null,
        photoWidth: Int? = null,
        photoHeight: Int? = null,
        needName: Boolean? = null,
        needPhoneNumber: Boolean? = null,
        needEmail: Boolean? = null,
        needShippingAddress: Boolean? = null,
        sendPhoneNumberToProvider: Boolean? = null,
        sendEmailToProvider: Boolean? = null,
        isFlexible: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ) = request<Message> {
        client.post("sendInvoice") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("title", title)
            parameter("description", description)
            parameter("payload", payload)
            parameter("provider_token", providerToken)
            parameter("currency", currency)
            parameter("prices", prices)
            parameter("max_tip_amount", maxTipAmount)
            parameter("suggested_tip_amounts", suggestedTipAmounts)
            parameter("start_parameter", startParameter)
            parameter("provider_data", providerData)
            parameter("photo_url", photoUrl)
            parameter("photo_size", photoSize)
            parameter("photo_width", photoWidth)
            parameter("photo_height", photoHeight)
            parameter("need_name", needName)
            parameter("need_phone_number", needPhoneNumber)
            parameter("need_email", needEmail)
            parameter("need_shipping_address", needShippingAddress)
            parameter("send_phone_number_to_provider", sendPhoneNumberToProvider)
            parameter("send_email_to_provider", sendEmailToProvider)
            parameter("is_flexible", isFlexible)
            parameter("disable_notification", disableNotification)
            parameter("reply_to_message_id", replyToMessageId)
            parameter("allow_sending_without_reply", allowSendingWithoutReply)
            parameter("reply_markup", replyMarkup)
        }
    }

    /**
     * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
     *
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes.
     * This will not be displayed to the user, use for your internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather)
     * @param currency Three-letter ISO 4217 currency code, see
     * [more on currencies](https://core.telegram.org/bots/payments#supported-currencies)
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount,
     * delivery cost, delivery tax, bonus, etc.)
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency
     * (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`.
     * See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json),
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     * Defaults to 0
     * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the *smallest units*
     * of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified.
     * The suggested tip amounts must be positive, passed in a strictly increased order and must
     * not exceed *max_tip_amount*.
     * @param providerData A JSON-serialized data about the invoice, which will be shared with the payment provider.
     * A detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing
     * image for a service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order
     * @param needEmail Pass *True*, if you require the user's email address to complete the order
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to provider
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to provider
     * @param isFlexible Pass *True*, if the final price depends on the shipping method
     * */
    suspend fun createInvoiceLink(
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: String,
        prices: String,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: String? = null,
        providerData: String? = null,
        photoUrl: String? = null,
        photoSize: Int? = null,
        photoWidth: Int? = null,
        photoHeight: Int? = null,
        needName: Boolean? = null,
        needPhoneNumber: Boolean? = null,
        needEmail: Boolean? = null,
        needShippingAddress: Boolean? = null,
        sendPhoneNumberToProvider: Boolean? = null,
        sendEmailToProvider: Boolean? = null,
        isFlexible: Boolean? = null,
    ) = request<NetworkResponse<String>> {
        client.post("createInvoiceLink") {
            parameter("title", title)
            parameter("description", description)
            parameter("payload", payload)
            parameter("provider_token", providerToken)
            parameter("currency", currency)
            parameter("prices", prices)
            parameter("max_tip_amount", maxTipAmount)
            parameter("suggested_tip_amounts", suggestedTipAmounts)
            parameter("provider_data", providerData)
            parameter("photo_url", photoUrl)
            parameter("photo_size", photoSize)
            parameter("photo_width", photoWidth)
            parameter("photo_height", photoHeight)
            parameter("need_name", needName)
            parameter("need_phone_number", needPhoneNumber)
            parameter("need_email", needEmail)
            parameter("need_shipping_address", needShippingAddress)
            parameter("send_phone_number_to_provider", sendPhoneNumberToProvider)
            parameter("send_email_to_provider", sendEmailToProvider)
            parameter("is_flexible", isFlexible)
        }
    }.result

    /**
     * If you sent an invoice requesting a shipping address and the parameter *is_flexible* was specified, the Bot API
     * will send an [Update][Update] with a *shipping_query* field to the bot.
     * Use this method to reply to shipping queries. On success, True is returned.
     *
     * @param shippingQueryId Unique identifier for the query to be answered
     * @param ok Specify True if delivery to the specified address is possible and False if there are any problems
     * (for example, if delivery to the specified address is not possible)
     * @param shippingOptions Required if *ok* is True. A JSON-serialized array of available shipping options.
     * @param errorMessage Required if *ok* is False. Error message in human readable form that explains why it is
     * impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable').
     * Telegram will display this message to the user.
     * */
    suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: String? = null,
        errorMessage: String? = null,
    ) = request<Boolean> {
        client.post("answerShippingQuery") {
            parameter("shipping_query_id", shippingQueryId)
            parameter("ok", ok)
            parameter("shipping_options", shippingOptions)
            parameter("error_message", errorMessage)
        }
    }

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation
     * in the form of an [Update][Update] with the field *pre_checkout_query*.
     * Use this method to respond to such pre-checkout queries.
     * On success, True is returned. **Note:** The Bot API must receive an answer within 10 seconds after
     * the pre-checkout query was sent.
     *
     * @param preCheckoutQueryId Unique identifier for the query to be answered
     * @param ok Specify *True* if everything is alright (goods are available, etc.) and the bot is ready to proceed
     * with the order. Use *False* if there are any problems.
     * @param errorMessage Required if *ok* is *False*. Error message in human readable form that explains the reason
     * for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black
     * T-shirts while you were busy filling out your payment details. Please choose a different color or garment!").
     * Telegram will display this message to the user.
     * */
    suspend fun answerPreCheckoutQuery(
        preCheckoutQueryId: String,
        ok: Boolean,
        errorMessage: String? = null,
    ) = request<Boolean> {
        client.post("answerPreCheckoutQuery") {
            parameter("pre_checkout_query_id", preCheckoutQueryId)
            parameter("ok", ok)
            parameter("error_message", errorMessage)
        }
    }

    suspend fun downloadFile(fileId: String) = downloadFile(getFile(fileId))

    suspend fun downloadFile(file: File) = request<InputStream> {
        client.get("${TelegramApi.BASE_URL}/file/bot${client.plugin(TokenAuthorization).token}/${file.path!!}") {
            unauthenticated()
        }
    }
}