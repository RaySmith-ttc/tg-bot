package ru.raysmith.tgbot.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.BotCommandsResponse
import ru.raysmith.tgbot.model.network.GetMeResponse
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.ChatAction
import ru.raysmith.tgbot.model.network.chat.GetChatResponse
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.file.FileResponse
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.message.*
import ru.raysmith.tgbot.model.network.updates.UpdatesResult

interface TelegramService {

    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     *
     * Returns basic information about the bot in form of a [User] object.
     * */
    @POST("getMe")
    fun getMe(): Call<GetMeResponse>

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally.
     * You **must** log out the bot before running it locally, otherwise there is no guarantee that the bot will
     * receive updates. After a successful call, you can immediately log in on a local server, but will not be able
     * to log in back to the cloud Bot API server for 10 minutes. Returns *True* on success. Requires no parameters.
     * */
    @POST("logOut")
    fun logOut(): Call<BooleanResponse>

    /**
     * Use this method to close the bot instance before moving it from one local server to another.
     * You need to delete the webhook before calling this method to ensure that the bot isn't launched again after
     * server restart. The method will return error 429 in the first 10 minutes after the bot is launched.
     * Returns *True* on success. Requires no parameters.
     * */
    @POST("close")
    fun close(): Call<BooleanResponse>

    /**
     * Use this method to send text messages. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
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
     * */
    @POST("sendMessage")
    fun sendMessage(
        @Query("chat_id") chatId: ChatId,
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
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of *parse_mode*
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     * Users will receive a notification with no sound.
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass *True*, if the message should be sent even if the specified replied-to message is not found
     * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to force a reply from the user.
     * */
    @POST("copyMessage")
    fun copyMessage(
        @Query("chat_id") chatId: ChatId,
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

    @POST("sendPhoto")
    fun sendPhoto(
        @Query("chat_id") chatId: ChatId,
        @Query("photo") photo: String,
        @Query("caption") caption: String? = null,
        @Query("caption_entities") captionEntities: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("disable_notification") disableNotification: Boolean? = null,
        @Query("protect_content") protectContent: Boolean? = null,
        @Query("reply_to_message_id") replyToMessageId: Int? = null,
        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @Multipart
    @POST("sendPhoto")
    fun sendPhoto(
        @Part("chat_id") chatId: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendAudio")
    fun sendAudio(
        @Query("chat_id") chatId: ChatId,
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
        @Part("chat_id") chatId: RequestBody,
        @Part audio: MultipartBody.Part,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("duration") duration: RequestBody? = null,
        @Part("performer") performer: RequestBody? = null,
        @Part("title") title: RequestBody? = null,
        @Part thumb: MultipartBody.Part? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendDocument")
    fun sendDocument(
        @Query("chat_id") chatId: ChatId,
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
        @Part("chat_id") chatId: RequestBody,
        @Part document: MultipartBody.Part,
        @Part thumb: MultipartBody.Part? = null,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("disable_content_type_detection") disableContentType_Detection: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVideo")
    fun sendVideo(
        @Query("chat_id") chatId: ChatId,
        @Query("video") video: String,
        @Query("duration") duration: Int? = null,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null,
        @Query("caption") caption: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null,
        @Query("caption_entities") captionEntities: String? = null,
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
        @Part("chat_id") chatId: RequestBody,
        @Part video: MultipartBody.Part,
        @Part("duration") duration: RequestBody? = null,
        @Part("width") width: RequestBody? = null,
        @Part("height") height: RequestBody? = null,
        @Part thumb: MultipartBody.Part? = null,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("supports_streaming") supportsStreaming: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

//    @POST("sendAnimation")
//    fun sendAnimation(
//        @Query("chat_id") chatId: ChatId,
//        @Query("animation") animation: String,
//        @Query("duration") duration: Int? = null,
//        @Query("width") width: Int? = null,
//        @Query("height") height: Int? = null,
//        @Query("caption") caption: String? = null,
//        @Query("parse_mode") parseMode: ParseMode? = null,
//        @Query("caption_entities") captionEntities: String? = null,
//        @Query("disable_notification") disableNotification: Boolean? = null,
//        @Query("protect_content") protectContent: Boolean? = null,
//        @Query("reply_to_message_id") replyToMessageId: Int? = null,
//        @Query("allow_sending_without_reply") allowSendingWithoutReply: Boolean? = null,
//        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
//    ): Call<MessageResponse>

    @Multipart
    @POST("sendAnimation")
    fun sendAnimation(
        @Part("chat_id") chatId: RequestBody,
        @Part animation: MultipartBody.Part,
        @Part("duration") duration: RequestBody? = null,
        @Part("width") width: RequestBody? = null,
        @Part("height") height: RequestBody? = null,
        @Part thumb: MultipartBody.Part? = null,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVoice")
    fun sendVoice(
        @Query("chat_id") chatId: ChatId,
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
        @Part("chat_id") chatId: RequestBody,
        @Part voice: MultipartBody.Part,
        @Part("caption") caption: RequestBody? = null,
        @Part("parse_mode") parseMode: RequestBody? = null,
        @Part("caption_entities") captionEntities: RequestBody? = null,
        @Part("duration") duration: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    @POST("sendVideoNote")
    fun sendVideoNote(
        @Query("chat_id") chatId: ChatId,
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
        @Part("chat_id") chatId: RequestBody,
        @Part videoNote: MultipartBody.Part,
        @Part("duration") duration: RequestBody? = null,
        @Part("length") length: RequestBody? = null,
        @Part("disable_notification") disableNotification: RequestBody? = null,
        @Part("protect_content") protectContent: RequestBody? = null,
        @Part("reply_to_message_id") replyToMessageId: RequestBody? = null,
        @Part("allow_sending_without_reply") allowSendingWithoutReply: RequestBody? = null,
        @Part("reply_markup") keyboardMarkup: RequestBody? = null
    ): Call<MessageResponse>

    /**
     * Use this method to get up to date information about the chat (current name of the user for one-on-one
     * conversations, current username of a user, group or channel, etc.).
     * Returns a [Chat][User] object on success.
     * */
    @POST("getChat")
    fun getChat(@Query("chat_id") chatId: ChatId): Call<GetChatResponse>

    @POST("getUpdates")
    fun getUpdates(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("timeout") timeout: Int? = null,
        @Query("allowed_updates") allowedUpdates: String? = null
    ): Call<UpdatesResult>

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
        @Query("media") media: String,
        @Part mediaPart1: MultipartBody.Part,
        @Part mediaPart2: MultipartBody.Part,
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
    ): Call<MessageResponse> // TODO On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.

    @POST("stopMessageLiveLocation")
    fun stopMessageLiveLocation(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("reply_markup") keyboardMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse> // TODO On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.

    @POST("sendVenue")
    fun sendVenue(
        @Query("chat_id") chatId: ChatId? = null,
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
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int? = null,
        @Query("caption") caption: String,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("parse_mode") parseMode: ParseMode? = null
    ): Call<MessageResponse>

    @POST("editMessageMedia")
    fun editMessageMedia(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int? = null,
        @Query("media") media: InputMedia,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null,
        @Query("inline_message_id") inlineMessageId: String? = null
    ): Call<MessageResponse>

    @POST("editMessageReplyMarkup")
    fun editMessageReplyMarkup(
        @Query("chat_id") chatId: ChatId? = null,
        @Query("message_id") messageId: Int? = null,
        @Query("inline_message_id") inlineMessageId: String? = null,
        @Query("reply_markup") replyMarkup: KeyboardMarkup? = null
    ): Call<MessageResponse>

    @POST("deleteMessage")
    fun deleteMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int
    ): Call<BooleanResponse>

    @POST("answerCallbackQuery")
    fun answerCallbackQuery(
        @Query("callback_query_id") callbackQueryId: String,
        @Query("text") text: String? = null,
        @Query("show_alert") showAlert: Boolean? = null,
        @Query("url") url: String? = null,
        @Query("cache_time") cacheTime: Int? = null
    ): Call<BooleanResponse>

    @POST("sendChatAction")
    fun sendChatAction(
        @Query("chat_id") chatId: ChatId,
        @Query("action") action: ChatAction
    ): Call<BooleanResponse>

    @POST("getFile")
    fun getFile(
        @Query("file_id") fileId: String
    ): Call<FileResponse>

    /**
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,
     * the user will not be able to return to the chat on their own using invite links, etc., unless [unbanned][unbanChatMember] first.
     * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
     * Returns True on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * (in the format `@channelusername`)
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are considered
     * to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     * */
    @POST("banChatMember")
    fun banChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: Long,
        @Query("until_date") untilDate: Int? = null,
        @Query("revoke_messages") revokeMessages: Boolean? = null
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
    @POST("unbanChatMember")
    fun unbanChatMember(
        @Query("chat_id") chatId: ChatId,
        @Query("user_id") userId: Long,
        @Query("only_if_banned") onlyIfBanned: Boolean? = null
    ): Call<BooleanResponse>

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * (in the format `@channelusername`)
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
     * @param photoSize Photo size
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
     * @param replyMarkup A JSON-serialized object for an
     * [inline keyboard](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating).
     * If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
     * */
    @POST("sendInvoice")
    fun sendInvoice(
        @Query("chat_id") chatId: ChatId,
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

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation
     * in the form of an [Update][ru.raysmith.tgbot.model.network.updates.Update] with the field *pre_checkout_query*.
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
    ): Call<BooleanResponse>

    /**
     * If you sent an invoice requesting a shipping address and the parameter *is_flexible* was specified, the Bot API
     * will send an [Update][ru.raysmith.tgbot.model.network.updates.Update] with a *shipping_query* field to the bot.
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
    ): Call<BooleanResponse>

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @see <a href="https://core.telegram.org/bots#commands">commands</a> for more details about bot commands. Returns True on success
     * */
    @POST("setMyCommands")
    fun setMyCommands(
        /** A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified. */
        @Query("commands") commandsJson: String,

        /** A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault]. */
        @Query("scope") scope: BotCommandScope? = null,

        /** A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands */
        @Query("language_code") languageCode: String? = null
    ): Call<BooleanResponse>

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language.
     * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns True on success.
     * */
    @POST("deleteMyCommands")
    fun deleteMyCommands(

        /** A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault]. */
        @Query("scope") scope: BotCommandScope? = null,

        /** A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands */
        @Query("language_code") languageCode: String? = null
    ): Call<BooleanResponse>

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user language.
     * Returns Array of [BotCommand] on success. If commands aren't set, an empty list is returned.
     * */
    @POST("getMyCommands")
    fun getMyCommands(

        /** A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault]. */
        @Query("scope") scope: BotCommandScope? = null,

        /** A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands */
        @Query("language_code") languageCode: String? = null
    ): Call<BotCommandsResponse>

    /**
     * Use this method to send answers to an inline query. On success, True is returned.
     * No more than 50 results per query are allowed.
     *
     * @param inlineQueryId Unique identifier for the answered query
     * @param results A JSON-serialized array of results for the inline query
     * @param cacheTime The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
     * @param isPersonal Pass True, if results may be cached on the server side only for the user that sent the query.
     * By default, results may be returned to any user who sends the same query
     * @param isPersonal Pass the offset that a client should send in the next query with the same text to receive
     * more results. Pass an empty string if there are no more results or if you don't support pagination.
     * Offset length can't exceed 64 bytes.
     * @param switchPmParameter [Deep-linking](https://core.telegram.org/bots#deep-linking) parameter
     * for the /start message sent to the bot when user presses the switch button.
     * 1-64 characters, only A-Z, a-z, 0-9, _ and - are allowed.
     *
     * Example: An inline bot that sends YouTube videos can ask the user to connect the bot to their YouTube account
     * to adapt search results accordingly. To do this, it displays a 'Connect your YouTube account' button above the
     * results, or even before showing any. The user presses the button, switches to a private chat with the bot and,
     * in doing so, passes a start parameter that instructs the bot to return an OAuth link. Once done, the bot can
     * offer a switch_inline button so that the user can easily return to the chat where they wanted to use the bot's
     * inline capabilities.
     * */
    @POST("answerInlineQuery")
    fun answerInlineQuery(
        @Query("inline_query_id") inlineQueryId: String,
        @Query("results") results: String,
        @Query("cache_time") cacheTime: Int? = null,
        @Query("is_personal") isPersonal: Boolean? = null,
        @Query("next_offset") nextOffset: String? = null,
        @Query("switch_pm_text") switchPmText: String? = null,
        @Query("switch_pm_parameter") switchPmParameter: String? = null,
    ): Call<BooleanResponse>

    @POST("pinChatMessage")
    fun pinChatMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int,
        @Query("disable_notification") disableNotification: Boolean? = null,
    ): Call<BooleanResponse>


    @POST("unpinChatMessage")
    fun unpinChatMessage(
        @Query("chat_id") chatId: ChatId,
        @Query("message_id") messageId: Int? = null
    ): Call<BooleanResponse>

    @POST("unpinAllChatMessages")
    fun unpinAllChatMessages(
        @Query("chat_id") chatId: ChatId
    ): Call<BooleanResponse>
}

