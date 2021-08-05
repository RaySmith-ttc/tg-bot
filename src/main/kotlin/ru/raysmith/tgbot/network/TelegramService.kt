package ru.raysmith.tgbot.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.GetMeResponse
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.chat.GetChatResponse
import ru.raysmith.tgbot.model.network.file.FileResponse
import ru.raysmith.tgbot.model.network.inputmedia.InputMedia
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.model.network.message.response.MediaSendResponse
import ru.raysmith.tgbot.model.network.message.response.MessageEditResponse
import ru.raysmith.tgbot.model.network.message.response.MessageSendResponse
import ru.raysmith.tgbot.model.network.updates.UpdatesResult

interface TelegramService {

    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     * Returns basic information about the bot in form of a User object.
     * */
    @GET("getMe")
    fun getMe(): Call<GetMeResponse>

    /**
     * Use this method to get up to date information about the chat (current name of the user for one-on-one
     * conversations, current username of a user, group or channel, etc.).
     * Returns a [Chat][ru.raysmith.tgbot.model.network.chat.Chat] object on success.
     * */
    @GET("getChat")
    fun getChat(@Query("chat_id") chatId: String): Call<GetChatResponse>

    @GET("getUpdates")
    fun getUpdates(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("timeout") timeout: Int? = null,
        @Query("allowed_updates") allowedUpdates: String? = null
    ): Call<UpdatesResult>

    /**
     *  Use this method to send text messages. On success, the sent [Message] is returned.
     *
     *  @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     *  @param text Text of the message to be sent, 1-4096 characters after entities parsing
     *  @param parseMode [Mode][ParseMode] for parsing entities in the message text.
     *  @param entities List of special entities that appear in message text, which can be specified instead of *parse_mode*
     *  @param disableWebPagePreview Disables link previews for links in this message
     *  @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     *  @param replyToMessageId If the message is a reply, ID of the original message
     *  @param allowSendingWithoutReply Pass True, if the message should be sent even if the specified replied-to message is not found
     *  @param keyboardMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     *  */
    @GET("sendMessage")
    fun sendMessage(
        @Query("chat_id") chatId: String,
        @Query("text") text: String,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("entities") entities: String? = null,
        @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageSendResponse>

    @GET("sendPhoto")
    fun sendPhoto(
        @Query("chat_id") chatId: String,
        @Query("photo") photo: String,
        @Query("caption") caption: String? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageSendResponse>

    @Multipart
    @POST("sendPhoto")
    fun sendPhotoPost(
        @Query("chat_id") chatId: String,
        @Part photo: MultipartBody.Part,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageSendResponse>

    @GET("sendDocument")
    fun sendDocument(
        @Query("chat_id") chatId: String,
        @Query("document") document: String,
    ): Call<MediaSendResponse>

    @GET("editMessageText")
    fun editMessageText(
        @Query("chat_id") chatId: String? = null,
        @Query("message_id") messageId: Long? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("text") text: String,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("entities") entities: String? = null,
        @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageEditResponse>

    @GET("editMessageCaption")
    fun editMessageCaption(
        @Query("chat_id") chatId: String,
        @Query("message_id") messageId: Int? = null,
        @Query("caption") caption: String,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null
    ): Call<MessageEditResponse>

    @GET("editMessageMedia")
    fun editMessageMedia(
        @Query("chat_id") chatId: String,
        @Query("message_id") messageId: Int? = null,
        @Query("media") media: InputMedia,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null,
        @Query("inline_message_id") inlineMessageId: String? = null
    ): Call<MessageEditResponse>

    @GET("editMessageReplyMarkup")
    fun editMessageReplyMarkup(
        @Query("chat_id") chatId: String,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null,
    ): Call<MessageEditResponse>

    @GET("deleteMessage")
    fun deleteMessage(
        @Query("chat_id") chatId: String,
        @Query("message_id") messageId: Long
    ): Call<BooleanResponse>

    @GET("answerCallbackQuery")
    fun answerCallbackQuery(
        @Query("callback_query_id") callbackQueryId: String,
        @Query("text") text: String? = null,
        @Query("show_alert") showAlert: Boolean? = null,
        @Query("url") url: String? = null,
        @Query("cache_time") cacheTime: Int? = null,
    ): Call<BooleanResponse>

    @GET("sendChatAction")
    fun sendChatAction(
        @Query("chat_id") chatId: String,
        @Query("action") action: ChatAction
    ): Call<BooleanResponse>

    @GET("getFile")
    fun getFile(
        @Query("file_id") fileId: String,
    ): Call<FileResponse>

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,
     * the user will not be able to return to the chat on their own using invite links, etc., unless [unbanned][unbanChatMember] first.
     * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
     * Returns True on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * (in the format @channelusername)
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are considered
     * to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     * */
    @GET("banChatMember")
    fun banChatMember(
        @Query("chat_id") chatId: String,
        @Query("user_id") userId: Long,
        @Query("until_date") untilDate: Int? = null,
        @Query("revoke_messages") revokeMessages: Boolean? = null,
    ): Call<BooleanResponse>

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the
     * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for
     * this to work. By default, this method guarantees that after the call the user is not a member of the chat,
     * but will be able to join it. So if the user is a member of the chat they will also be **removed** from the chat.
     * If you don't want this, use the parameter only_if_banned. Returns True on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * (in the format @username)
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     * */
    @GET("unbanChatMember")
    fun unbanChatMember(
        @Query("chat_id") chatId: String,
        @Query("user_id") userId: Long,
        @Query("only_if_banned") onlyIfBanned: Boolean? = null,
    ): Call<BooleanResponse>

}

