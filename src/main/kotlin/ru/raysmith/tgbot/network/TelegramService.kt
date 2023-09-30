package ru.raysmith.tgbot.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.raysmith.tgbot.model.bot.BotDescription
import ru.raysmith.tgbot.model.bot.BotName
import ru.raysmith.tgbot.model.bot.BotShortDescription
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.Poll
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.WebhookInfo
import ru.raysmith.tgbot.model.network.chat.*
import ru.raysmith.tgbot.model.network.chat.forum.ForumTopic
import ru.raysmith.tgbot.model.network.chat.forum.IconColor
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.file.File
import ru.raysmith.tgbot.model.network.file.FileResponse
import ru.raysmith.tgbot.model.network.inline.SentWebAppMessage
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.menubutton.MenuButton
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.PollType
import ru.raysmith.tgbot.model.network.response.*
import ru.raysmith.tgbot.model.network.sticker.*
import ru.raysmith.tgbot.model.network.updates.Update

interface TelegramService {

    @POST("getUpdates")
    fun getUpdates(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("timeout") timeout: Int? = null,
        @Query("allowed_updates") allowedUpdates: String? = null
    ): Call<NetworkResponse<List<Update>>>

    @Multipart
    @POST("setWebhook")
    fun setWebhook(
        @Query("url") url: RequestBody,
        @Part certificate: MultipartBody.Part? = null,
        @Query("ip_address") ipAddress: RequestBody? = null,
        @Query("max_connections") maxConnections: RequestBody? = null,
        @Query("allowed_updates") allowedUpdates: RequestBody? = null,
        @Query("drop_pending_updates") dropPendingUpdates: RequestBody? = null,
        @Query("secret_token") secretToken: RequestBody? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteWebhook")
    fun deleteWebhook(
        @Query("drop_pending_updates") dropPendingUpdates: Boolean? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("getWebhookInfo")
    fun getWebhookInfo(): Call<NetworkResponse<WebhookInfo>>

    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     *
     * Returns basic information about the bot in form of a [User] object.
     * */
    @POST("getMe")
    fun getMe(): Call<NetworkResponse<User>>

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally.
     * You **must** log out the bot before running it locally, otherwise there is no guarantee that the bot will
     * receive updates. After a successful call, you can immediately log in on a local server, but will not be able
     * to log in back to the cloud Bot API server for 10 minutes. Returns *True* on success. Requires no parameters.
     * */
    @POST("logOut")
    fun logOut(): Call<NetworkResponse<Boolean>>

    /**
     * Use this method to close the bot instance before moving it from one local server to another.
     * You need to delete the webhook before calling this method to ensure that the bot isn't launched again after
     * server restart. The method will return error 429 in the first 10 minutes after the bot is launched.
     * Returns *True* on success. Requires no parameters.
     * */
    @POST("close")
    fun close(): Call<NetworkResponse<Boolean>>

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
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
     * */
    @POST("sendMessage")
    fun sendMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("text") text: String,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("entities") entities: String? = null,
        @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    /**
     * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * */
    @POST("forwardMessage")
    fun forwardMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("from_chat_id") fromChatId: ChatId,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("message_id") messageId: Int
    ): Call<MessageResponse>

    /**
     * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
     * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the
     * original message. Returns the MessageId of the sent message on success.
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
     * @param parseMode [ParseMode] for parsing entities in the message caption.
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of *[parseMode]*
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param replyMarkup Additional interface options. Object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * */
    @POST("copyMessage")
    fun copyMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("from_chat_id") fromChatId: ChatId,
        @Query("message_id") messageId: Int,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageIdResponse>

    @Multipart
    @POST("sendPhoto")
    fun sendPhoto(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part photo: MultipartBody.Part,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("has_spoiler") hasSpoiler: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendPhoto")
    fun sendPhoto(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("photo") photo: String,
        @Query("caption") caption: String? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("has_spoiler") hasSpoiler: Boolean? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("sendAudio")
    fun sendAudio(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("audio") audio: String,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("duration") duration: Int? = null,
        @Query("performer") performer: String? = null,
        @Query("title") title: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendAudio")
    fun sendAudio(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part audio: MultipartBody.Part,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("duration") duration: RequestBody? = null,
        @Query("performer") performer: RequestBody? = null,
        @Query("title") title: RequestBody? = null,
        @Part thumbnail: MultipartBody.Part? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendDocument")
    fun sendDocument(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("document") document: String,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("disable_content_type_detection") disableContentTypeDetection: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendDocument")
    fun sendDocument(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part document: MultipartBody.Part,
        @Part thumbnail: MultipartBody.Part? = null,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("disable_content_type_detection") disableContentTypeDetection: RequestBody? = null, // TODO not using
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVideo")
    fun sendVideo(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("video") video: String,
        @Query("duration") duration: Int? = null,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("has_spoiler") hasSpoiler: Boolean? = null,
        @Query("supports_streaming") supportsStreaming: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendVideo")
    fun sendVideo(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part video: MultipartBody.Part,
        @Query("duration") duration: RequestBody? = null,
        @Query("width") width: RequestBody? = null,
        @Query("height") height: RequestBody? = null,
        @Part thumbnail: MultipartBody.Part? = null,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("has_spoiler") hasSpoiler: RequestBody? = null,
        @Query("supports_streaming") supportsStreaming: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendVideo")
    fun sendVideo(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Query("video") video: RequestBody,
        @Query("duration") duration: RequestBody? = null,
        @Query("width") width: RequestBody? = null,
        @Query("height") height: RequestBody? = null,
        @Part thumbnail: MultipartBody.Part? = null,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("has_spoiler") hasSpoiler: RequestBody? = null,
        @Query("supports_streaming") supportsStreaming: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendAnimation")
    fun sendAnimation(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("animation") animation: String,
        @Query("duration") duration: Int? = null,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null,
        @Query("thumbnail") thumbnail: String? = null,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("has_spoiler") hasSpoiler: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendAnimation")
    fun sendAnimation(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part animation: MultipartBody.Part,
        @Query("duration") duration: RequestBody? = null,
        @Query("width") width: RequestBody? = null,
        @Query("height") height: RequestBody? = null,
        @Part thumbnail: MultipartBody.Part? = null,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("has_spoiler") hasSpoiler: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVoice")
    fun sendVoice(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("voice") voice: String,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("duration") duration: Int? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendVoice")
    fun sendVoice(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part voice: MultipartBody.Part,
        @Query("caption") caption: RequestBody? = null,
        @Query("parse_mode") parseMode: RequestBody? = null,
        @Query("caption_entities") captionEntities: RequestBody? = null,
        @Query("duration") duration: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVideoNote")
    fun sendVideoNote(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("video_note") videoNote: String,
        @Query("duration") duration: Int? = null,
        @Query("length") length: Int? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendVideoNote")
    fun sendVideoNote(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part videoNote: MultipartBody.Part,
        @Query("duration") duration: RequestBody? = null,
        @Query("length") length: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

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
    @POST("sendMediaGroup")
    fun sendMediaGroup(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("media") media: String,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null
    ): Call<MessageResponseArray>

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
    @Multipart
    @POST("sendMediaGroup")
    fun sendMediaGroup(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("media") media: String,
        @Part mediaPart1: MultipartBody.Part,
        @Part mediaPart2: MultipartBody.Part? = null,
        @Part mediaPart3: MultipartBody.Part? = null,
        @Part mediaPart4: MultipartBody.Part? = null,
        @Part mediaPart5: MultipartBody.Part? = null,
        @Part mediaPart6: MultipartBody.Part? = null,
        @Part mediaPart7: MultipartBody.Part? = null,
        @Part mediaPart8: MultipartBody.Part? = null,
        @Part mediaPart9: MultipartBody.Part? = null,
        @Part mediaPart10: MultipartBody.Part? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null
    ): Call<MessageResponseArray>

    @POST("sendLocation")
    fun sendLocation(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("horizontal_accuracy") horizontalAccuracy: Double? = null,
        @Query("live_period") livePeriod: Int? = null,
        @Query("heading") heading: Int? = null,
        @Query("proximity_alert_radius") proximityAlertRadius: Int? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("editMessageLiveLocation")
    fun editMessageLiveLocation(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("horizontal_accuracy") horizontalAccuracy: Double? = null,
        @Query("heading") heading: Int? = null,
        @Query("proximity_alert_radius") proximityAlertRadius: Int? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<LiveLocationResponse>

    @POST("stopMessageLiveLocation")
    fun stopMessageLiveLocation(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<LiveLocationResponse>

    @POST("sendVenue")
    fun sendVenue(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("title") title: String,
        @Query("address") address: String,
        @Query("foursquare_id") foursquareId: String? = null,
        @Query("foursquare_type") foursquareType: String? = null,
        @Query("google_place_id") googlePlaceId: String? = null,
        @Query("google_place_type") googlePlaceType: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("sendContact")
    fun sendContact(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("phone_number") phoneNumber: String,
        @Query("first_name") firstName: String,
        @Query("last_name") lastName: String? = null,
        @Query("vcard") vcard: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("sendPoll")
    fun sendPoll(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("question") question: String,
        @Query("options") options: String,
        @Query("is_anonymous") isAnonymous: Boolean? = null,
        @Query("type") type: PollType? = null,
        @Query("allows_multiple_answers") allowsMultipleAnswers: Boolean? = null,
        @Query("correct_option_id") correctOptionId: Int? = null,
        @Query("explanation") explanation: String? = null,
        @Query("explanation_parse_mode") explanationParseMode: String? = null,
        @Query("explanation_entities") explanationEntities: String? = null,
        @Query("open_period") openPeriod: Int? = null,
        @Query("close_date") closeDate: Int? = null,
        @Query("is_closed") isClosed: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("sendDice")
    fun sendDice(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("emoji") emoji: String,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("sendChatAction")
    fun sendChatAction(
        @Query("chat_id") chatId: ChatId,
        @Query("action") action: ChatAction,
        @Query("message_thread_id") messageThreadId: Int? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("getUserProfilePhotos")
    fun getUserProfilePhotos(
        @Query("user_id") userId: ChatId.ID,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
    ): Call<UserProfilePhotosResponse>

    @POST("getFile")
    fun getFile(
        @Query("file_id") fileId: String
    ): Call<FileResponse>

    @POST("banChatMember")
    fun banChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
        @Query("until_date") untilDate: Int? = null,
        @Query("revoke_messages") revokeMessages: Boolean? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("unbanChatMember")
    fun unbanChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
        @Query("only_if_banned") onlyIfBanned: Boolean? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("restrictChatMember")
    fun restrictChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
        @Query("permissions") permissions: ChatPermissions,
        @Query("use_independent_chat_permissions") useIndependentChatPermissions: Boolean? = null,
        @Query("until_date") untilDate: Int? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("promoteChatMember")
    fun promoteChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
        @Query("is_anonymous") isAnonymous: Boolean? = null,
        @Query("can_manage_chat") canManageChat: Boolean? = null,
        @Query("can_post_messages") canPostMessages: Boolean? = null,
        @Query("can_edit_messages") canEditMessages: Boolean? = null,
        @Query("can_delete_messages") canDeleteMessages: Boolean? = null,
        @Query("can_post_stories") canPostStories: Boolean? = null,
        @Query("can_edit_stories") canEditStories: Boolean? = null,
        @Query("can_delete_stories") canDeleteStories: Boolean? = null,
        @Query("can_manage_video_chats") canManageVideoChats: Boolean? = null,
        @Query("can_restrict_members") canRestrictMembers: Boolean? = null,
        @Query("can_promote_members") canPromoteMembers: Boolean? = null,
        @Query("can_change_info") canChangeInfo: Boolean? = null,
        @Query("can_invite_users") canInviteUsers: Boolean? = null,
        @Query("can_pin_messages") canPinMessages: Boolean? = null,
        @Query("can_manage_topics") canManageTopics: Boolean? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("setChatAdministratorCustomTitle")
    fun setChatAdministratorCustomTitle(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
        @Query("custom_title") customTitle: String,
    ): Call<NetworkResponse<Boolean>>

    @POST("banChatSenderChat")
    fun banChatSenderChat(
        @Query("chat_id") chatId: ChatId,
        @Query("sender_chat_id") senderChatId: ChatId.ID,
    ): Call<NetworkResponse<Boolean>>

    @POST("unbanChatSenderChat")
    fun unbanChatSenderChat(
        @Query("chat_id") chatId: ChatId,
        @Query("sender_chat_id") senderChatId: ChatId.ID,
    ): Call<NetworkResponse<Boolean>>

    @POST("setChatPermissions")
    fun setChatPermissions(
        @Query("chat_id") chatId: ChatId,
        @Query("permissions") permissions: ChatPermissions,
        @Query("use_independent_chat_permissions") useIndependentChatPermissions: Boolean? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("exportChatInviteLink")
    fun exportChatInviteLink(
        @Query("chat_id") chatId: ChatId
    ): Call<StringResponse>

    @POST("createChatInviteLink")
    fun createChatInviteLink(
        @Query("chat_id") chatId: ChatId,
        @Query("name") name: String? = null,
        @Query("expire_date") expireDate: Int? = null,
        @Query("member_limit") memberLimit: Int? = null,
        @Query("creates_join_request") createsJoinRequest: Boolean? = null,
    ): Call<ChatInviteLinkResponse>

    @POST("editChatInviteLink")
    fun editChatInviteLink(
        @Query("chat_id") chatId: ChatId,
        @Query("invite_link") inviteLink: String,
        @Query("name") name: String? = null,
        @Query("expire_date") expireDate: Int? = null,
        @Query("member_limit") memberLimit: Int? = null,
        @Query("creates_join_request") createsJoinRequest: Boolean? = null,
    ): Call<ChatInviteLinkResponse>

    @POST("revokeChatInviteLink")
    fun revokeChatInviteLink(
        @Query("chat_id") chatId: ChatId,
        @Query("invite_link") inviteLink: String,
    ): Call<ChatInviteLinkResponse>

    @POST("approveChatJoinRequest")
    fun approveChatJoinRequest(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
    ): Call<NetworkResponse<Boolean>>

    @POST("declineChatJoinRequest")
    fun declineChatJoinRequest(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID,
    ): Call<NetworkResponse<Boolean>>

    @Multipart
    @POST("setChatPhoto")
    fun setChatPhoto(
        @Query("chat_id") chatId: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteChatPhoto")
    fun deleteChatPhoto(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("setChatTitle")
    fun setChatTitle(
        @Query("chat_id") chatId: ChatId,
        @Query("title") title: String
    ): Call<NetworkResponse<Boolean>>

    @POST("setChatDescription")
    fun setChatDescription(
        @Query("chat_id") chatId: ChatId,
        @Query("description") description: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("pinChatMessage")
    fun pinChatMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int,
        @Query("disable_notification") disableNotification: Boolean? = null,
    ): Call<NetworkResponse<Boolean>>


    @POST("unpinChatMessage")
    fun unpinChatMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("unpinAllChatMessages")
    fun unpinAllChatMessages(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("leaveChat")
    fun leaveChat(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("getChat")
    fun getChat(
        @Query("chat_id") chatId: ChatId
    ): Call<GetChatResponse>

    @POST("getChatAdministrators")
    fun getChatAdministrators(
        @Query("chat_id") chatId: ChatId
    ): Call<ChatMembersResponse>

    @POST("getChatMemberCount")
    fun getChatMemberCount(
        @Query("chat_id") chatId: ChatId
    ): Call<IntResponse>

    @POST("getChatMember")
    fun getChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: ChatId.ID
    ): Call<ChatMemberResponse>

    @POST("setChatStickerSet")
    fun setChatStickerSet(
        @Query("chat_id") chatId: ChatId,
        @Query("sticker_set_name") stickerSetName: String
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteChatStickerSet")
    fun deleteChatStickerSet(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("getForumTopicIconStickers")
    fun getForumTopicIconStickers(): Call<NetworkResponse<List<Sticker>>>

    @POST("createForumTopic")
    fun createForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("name") name: String,
        @Query("icon_color") iconColor: IconColor? = null,
        @Query("icon_custom_emoji_id") iconCustomEmojiId: String? = null,
    ): Call<NetworkResponse<ForumTopic>>

    @POST("editForumTopic")
    fun editForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("name") name: String? = null,
        @Query("icon_custom_emoji_id") iconCustomEmojiId: String? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("closeForumTopic")
    fun closeForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("reopenForumTopic")
    fun reopenForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteForumTopic")
    fun deleteForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("unpinAllForumTopicMessages")
    fun unpinAllForumTopicMessages(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("editGeneralForumTopic")
    fun editGeneralForumTopic(
        @Query("chat_id") chatId: ChatId,
        @Query("name") name: String,
    ): Call<NetworkResponse<Boolean>>

    @POST("closeGeneralForumTopic")
    fun closeGeneralForumTopic(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("reopenGeneralForumTopic")
    fun reopenGeneralForumTopic(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("hideGeneralForumTopic")
    fun hideGeneralForumTopic(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("unhideGeneralForumTopic")
    fun unhideGeneralForumTopic(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("unpinAllGeneralForumTopicMessages")
    fun unpinAllGeneralForumTopicMessages(
        @Query("chat_id") chatId: ChatId
    ): Call<NetworkResponse<Boolean>>

    @POST("answerCallbackQuery")
    fun answerCallbackQuery(
        @Query("callback_query_id") callbackQueryId: String,
        @Query("text") text: String? = null,
        @Query("show_alert") showAlert: Boolean? = null,
        @Query("url") url: String? = null,
        @Query("cache_time") cacheTime: Int? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("setMyCommands")
    fun setMyCommands(
        @Query("commands") commandsJson: String,
        @Query("scope") scope: BotCommandScope? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteMyCommands")
    fun deleteMyCommands(
        @Query("scope") scope: BotCommandScope? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("getMyCommands")
    fun getMyCommands(
        @Query("scope") scope: BotCommandScope? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<List<BotCommand>>>

    @POST("setMyName")
    fun setMyName(
        @Query("name") name: String? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("getMyName")
    fun getMyName(
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<BotName>>

    @POST("setMyDescription")
    fun setMyDescription(
        @Query("description") description: String? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("getMyDescription")
    fun getMyDescription(
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<BotDescription>>

    @POST("setMyShortDescription")
    fun setMyShortDescription(
        @Query("short_description") shortDescription: String? = null,
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<Boolean>>

    @POST("getMyShortDescription")
    fun getMyShortDescription(
        @Query("language_code") languageCode: String? = null
    ): Call<NetworkResponse<BotShortDescription>>

    @POST("setChatMenuButton")
    fun setChatMenuButton(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("menu_button") menuButton: String? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("getChatMenuButton")
    fun getChatMenuButton(
        @Query("chat_id") chatId: ChatId? = null
    ): Call<NetworkResponse<MenuButton>>

    @POST("setMyDefaultAdministratorRights")
    fun setMyDefaultAdministratorRights(
        @Query("rights") rights: ChatAdministratorRights? = null,
        @Query("for_channels") forChannels: Boolean? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("getMyDefaultAdministratorRights")
    fun getMyDefaultAdministratorRights(
        @Query("for_channels") forChannels: Boolean? = null,
    ): Call<NetworkResponse<ChatAdministratorRights>>

    @POST("editMessageText")
    fun editMessageText(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("text") text: String,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("entities") entities: String? = null,
        @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("editMessageCaption")
    fun editMessageCaption(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("caption") caption: String,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") entities: String? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("editMessageMedia")
    fun editMessageMedia(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("media") media: InputMedia,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("editMessageReplyMarkup")
    fun editMessageReplyMarkup(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("stopPoll")
    fun stopPoll(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<NetworkResponse<Poll>>

    @POST("deleteMessage")
    fun deleteMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int
    ): Call<NetworkResponse<Boolean>>

    @POST("sendSticker")
    fun sendSticker(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("sticker") sticker: String,
        @Query("emoji") emoji: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendSticker")
    fun sendSticker(
        @Query("chat_id") chatId: RequestBody,
        @Query("message_thread_id") messageThreadId: RequestBody? = null,
        @Part sticker: MultipartBody.Part,
        @Query("emoji") emoji: RequestBody? = null,
        @Query("disable_notification") disableNotification: RequestBody? = null,
        @Query("protect_content") protectContent: RequestBody? = null,
        @Query("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Query("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("getStickerSet")
    fun getStickerSet(
        @Query("name") name: String
    ): Call<NetworkResponse<StickerSet>>

    @POST("getCustomEmojiStickers")
    fun getCustomEmojiStickers(
        @Query("custom_emoji_ids") customEmojiIds: String // TODO List<String> not work; check other methods
    ): Call<NetworkResponse<List<Sticker>>>

    @POST("uploadStickerFile")
    fun uploadStickerFile(
        @Query("user_id") userId: ChatId.ID,
        @Query("sticker") sticker: String,
        @Query("sticker_format") stickerFormat: StickerFormat
    ): Call<NetworkResponse<File>>

    @Multipart
    @POST("uploadStickerFile")
    fun uploadStickerFile(
        @Query("user_id") userId: ChatId.ID,
        @Part sticker: MultipartBody.Part,
        @Query("sticker_format") stickerFormat: StickerFormat
    ): Call<NetworkResponse<File>>

//    @POST("createNewStickerSet")
//    fun createNewStickerSet(
//        @Query("user_id") userId: ChatId.ID,
//        @Query("name") name: String,
//        @Query("title") title: String,
//        @Query("stickers") stickers: String,
//        @Query("sticker_format") stickerFormat: StickerFormat? = null,
//        @Query("sticker_type") stickerType: StickerType? = null,
//        @Query("needs_repainting") needsRepainting: Boolean? = null,
//    ): Call<NetworkResponse<Boolean>>

    @Multipart
    @POST("createNewStickerSet")
    fun createNewStickerSet(
        @Query("user_id") userId: ChatId.ID,
        @Query("name") name: String,
        @Query("title") title: String,
        @Query("stickers") stickers: String,
        @Query("sticker_format") stickerFormat: StickerFormat? = null,
        @Query("sticker_type") stickerType: StickerType? = null,
        @Query("needs_repainting") needsRepainting: Boolean? = null,

        @Part mediaPart1: MultipartBody.Part? = null,
        @Part mediaPart2: MultipartBody.Part? = null,
        @Part mediaPart3: MultipartBody.Part? = null,
        @Part mediaPart4: MultipartBody.Part? = null,
        @Part mediaPart5: MultipartBody.Part? = null,
        @Part mediaPart6: MultipartBody.Part? = null,
        @Part mediaPart7: MultipartBody.Part? = null,
        @Part mediaPart8: MultipartBody.Part? = null,
        @Part mediaPart9: MultipartBody.Part? = null,
        @Part mediaPart10: MultipartBody.Part? = null,
        @Part mediaPart11: MultipartBody.Part? = null,
        @Part mediaPart12: MultipartBody.Part? = null,
        @Part mediaPart13: MultipartBody.Part? = null,
        @Part mediaPart14: MultipartBody.Part? = null,
        @Part mediaPart15: MultipartBody.Part? = null,
        @Part mediaPart16: MultipartBody.Part? = null,
        @Part mediaPart17: MultipartBody.Part? = null,
        @Part mediaPart18: MultipartBody.Part? = null,
        @Part mediaPart19: MultipartBody.Part? = null,
        @Part mediaPart20: MultipartBody.Part? = null,
        @Part mediaPart21: MultipartBody.Part? = null,
        @Part mediaPart22: MultipartBody.Part? = null,
        @Part mediaPart23: MultipartBody.Part? = null,
        @Part mediaPart24: MultipartBody.Part? = null,
        @Part mediaPart25: MultipartBody.Part? = null,
        @Part mediaPart26: MultipartBody.Part? = null,
        @Part mediaPart27: MultipartBody.Part? = null,
        @Part mediaPart28: MultipartBody.Part? = null,
        @Part mediaPart29: MultipartBody.Part? = null,
        @Part mediaPart30: MultipartBody.Part? = null,
        @Part mediaPart31: MultipartBody.Part? = null,
        @Part mediaPart32: MultipartBody.Part? = null,
        @Part mediaPart33: MultipartBody.Part? = null,
        @Part mediaPart34: MultipartBody.Part? = null,
        @Part mediaPart35: MultipartBody.Part? = null,
        @Part mediaPart36: MultipartBody.Part? = null,
        @Part mediaPart37: MultipartBody.Part? = null,
        @Part mediaPart38: MultipartBody.Part? = null,
        @Part mediaPart39: MultipartBody.Part? = null,
        @Part mediaPart40: MultipartBody.Part? = null,
        @Part mediaPart41: MultipartBody.Part? = null,
        @Part mediaPart42: MultipartBody.Part? = null,
        @Part mediaPart43: MultipartBody.Part? = null,
        @Part mediaPart44: MultipartBody.Part? = null,
        @Part mediaPart45: MultipartBody.Part? = null,
        @Part mediaPart46: MultipartBody.Part? = null,
        @Part mediaPart47: MultipartBody.Part? = null,
        @Part mediaPart48: MultipartBody.Part? = null,
        @Part mediaPart49: MultipartBody.Part? = null,
        @Part mediaPart50: MultipartBody.Part? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("addStickerToSet")
    fun addStickerToSet(
        @Query("user_id") userId: ChatId.ID,
        @Query("name") name: String,
        @Query("sticker") sticker: String,
    ): Call<NetworkResponse<Boolean>>

    @Multipart
    @POST("addStickerToSet")
    fun addStickerToSet(
        @Query("user_id") userId: ChatId.ID,
        @Query("name") name: String,
        @Query("sticker") sticker: String,
        @Part file: MultipartBody.Part? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("setStickerPositionInSet")
    fun setStickerPositionInSet(
        @Query("sticker") sticker: String,
        @Query("position") position: Int,
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteStickerFromSet")
    fun deleteStickerFromSet(
        @Query("sticker") sticker: String,
    ): Call<NetworkResponse<Boolean>>

    @POST("setStickerEmojiList")
    fun setStickerEmojiList(
        @Query("sticker") sticker: String,
        @Query("emoji_list") emojiList: List<String>,
    ): Call<NetworkResponse<Boolean>>

    @POST("setStickerKeywords")
    fun setStickerKeywords(
        @Query("sticker") sticker: String,
        @Query("keywords") keywords: List<String>?,
    ): Call<NetworkResponse<Boolean>>

    @POST("setStickerMaskPosition")
    fun setStickerMaskPosition(
        @Query("sticker") sticker: String,
        @Query("mask_position") maskPosition: MaskPosition?,
    ): Call<NetworkResponse<Boolean>>

    @POST("setStickerSetTitle")
    fun setStickerSetTitle(
        @Query("name") name: String,
        @Query("title") title: String,
    ): Call<NetworkResponse<Boolean>>

    @Multipart
    @POST("setStickerSetThumbnail")
    fun setStickerSetThumbnail(
        @Query("name") name: String,
        @Query("user_id") userId: ChatId.ID,
        @Part thumbnail: MultipartBody.Part? = null,
    ): Call<NetworkResponse<Boolean>>

    @POST("setCustomEmojiStickerSetThumbnail")
    fun setCustomEmojiStickerSetThumbnail(
        @Query("name") name: String,
        @Query("custom_emoji_id") customEmojiId: String?,
    ): Call<NetworkResponse<Boolean>>

    @POST("deleteStickerSet")
    fun deleteStickerSet(
        @Query("name") name: String,
    ): Call<NetworkResponse<Boolean>>

    @POST("answerInlineQuery")
    fun answerInlineQuery(
        @Query("inline_query_id") inlineQueryId: String,
        @Query("results") results: String,
        @Query("cache_time") cacheTime: Int? = null,
        @Query("is_personal") isPersonal: Boolean? = null,
        @Query("next_offset") nextOffset: String? = null,
        @Query("button") button: String? = null
    ): Call<NetworkResponse<Boolean>>

    // TODO test
    @POST("answerWebAppQuery")
    fun answerWebAppQuery(
        @Query("web_app_query_id") webAppQueryId: String,
        @Query("result") result: InlineQueryResult,
    ): Call<NetworkResponse<SentWebAppMessage>>

    @POST("sendInvoice")
    fun sendInvoice(
        @Query("chat_id") chatId: ChatId,
        @Query("message_thread_id") messageThreadId: Int? = null,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("payload") payload: String,
        @Query("provider_token") providerToken: String,
        @Query("currency") currency: String,
        @Query("prices") prices: String,
        @Query("max_tip_amount") maxTipAmount: Int? = null,
        @Query("suggested_tip_amounts") suggestedTipAmounts: String? = null,
        @Query("start_parameter") startParameter: String? = null,
        @Query("provider_data") providerData: String? = null,
        @Query("photo_url") photoUrl: String? = null,
        @Query("photo_size") photoSize: Int? = null,
        @Query("photo_width") photoWidth: Int? = null,
        @Query("photo_height") photoHeight: Int? = null,
        @Query("need_name") needName: Boolean? = null,
        @Query("need_phone_number") needPhoneNumber: Boolean? = null,
        @Query("need_email") needEmail: Boolean? = null,
        @Query("need_shipping_address") needShippingAddress: Boolean? = null,
        @Query("send_phone_number_to_provider") sendPhoneNumberToProvider: Boolean? = null,
        @Query("send_email_to_provider") sendEmailToProvider: Boolean? = null,
        @Query("is_flexible") isFlexible: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Long? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") replyMarkup: InlineKeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("createInvoiceLink")
    fun createInvoiceLink(
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("payload") payload: String,
        @Query("provider_token") providerToken: String,
        @Query("currency") currency: String,
        @Query("prices") prices: String,
        @Query("max_tip_amount") maxTipAmount: Int? = null,
        @Query("suggested_tip_amounts") suggestedTipAmounts: String? = null,
        @Query("provider_data") providerData: String? = null,
        @Query("photo_url") photoUrl: String? = null,
        @Query("photo_size") photoSize: Int? = null,
        @Query("photo_width") photoWidth: Int? = null,
        @Query("photo_height") photoHeight: Int? = null,
        @Query("need_name") needName: Boolean? = null,
        @Query("need_phone_number") needPhoneNumber: Boolean? = null,
        @Query("need_email") needEmail: Boolean? = null,
        @Query("need_shipping_address") needShippingAddress: Boolean? = null,
        @Query("send_phone_number_to_provider") sendPhoneNumberToProvider: Boolean? = null,
        @Query("send_email_to_provider") sendEmailToProvider: Boolean? = null,
        @Query("is_flexible") isFlexible: Boolean? = null,
    ): Call<StringResponse>

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
    @POST("answerShippingQuery")
    fun answerShippingQuery(
        @Query("shipping_query_id") shippingQueryId: String,
        @Query("ok") ok: Boolean,
        @Query("shipping_options") shippingOptions: String? = null,
        @Query("error_message") errorMessage: String? = null
    ): Call<NetworkResponse<Boolean>>

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
    @POST("answerPreCheckoutQuery")
    fun answerPreCheckoutQuery(
        @Query("pre_checkout_query_id") preCheckoutQueryId: String,
        @Query("ok") ok: Boolean,
        @Query("error_message") errorMessage: String? = null
    ): Call<NetworkResponse<Boolean>>

//    abstract fun sendVideo(
//        chatId: ChatId,
//        messageThreadId: Int?,
//        video: InputFile,
//        caption: String?,
//        parseMode: ParseMode?,
//        captionEntities: String?,
//        hasSpoiler: Boolean?,
//        duration: Int?,
//        width: Int?,
//        height: Int?,
//        supportsStreaming: Boolean?,
//        disableNotification: Boolean?,
//        protectContent: Boolean?,
//        replyToMessageId: Int?,
//        allowSendingWithoutReply: Boolean?,
//        keyboardMarkup: KeyboardMarkup?
//    ): Message

    // TODO [passport] setPassportDataErrors

    // TODO [games] sendGame
    // TODO [games] setGameScore
    // TODO [games] getGameHighScores
}

