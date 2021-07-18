package ru.raysmith.tgbot.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.GetMeResponse
import ru.raysmith.tgbot.model.network.chat.ChatAction
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
        @Query("message_id") messageId: Int
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

}

