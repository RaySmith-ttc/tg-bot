@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.raysmith.tgbot.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.api.BotAccessSettings
import ru.raysmith.tgbot.model.api.File
import ru.raysmith.tgbot.model.api.StarAmount
import ru.raysmith.tgbot.model.api.WebhookInfo
import ru.raysmith.tgbot.model.api.business.BusinessBotRights
import ru.raysmith.tgbot.model.api.business.BusinessConnection
import ru.raysmith.tgbot.model.api.chat.*
import ru.raysmith.tgbot.model.api.chat.forum.ForumTopic
import ru.raysmith.tgbot.model.api.chat.forum.IconColor
import ru.raysmith.tgbot.model.api.chat.member.ChatMember
import ru.raysmith.tgbot.model.api.chat.member.ChatMemberAdministrator
import ru.raysmith.tgbot.model.api.command.BotCommand
import ru.raysmith.tgbot.model.api.command.BotCommandScope
import ru.raysmith.tgbot.model.api.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.api.gift.AcceptedGiftTypes
import ru.raysmith.tgbot.model.api.gift.Gift
import ru.raysmith.tgbot.model.api.gift.GiftInfo
import ru.raysmith.tgbot.model.api.gift.Gifts
import ru.raysmith.tgbot.model.api.inline.PreparedInlineMessage
import ru.raysmith.tgbot.model.api.inline.PreparedKeyboardButton
import ru.raysmith.tgbot.model.api.inline.SentGuestMessage
import ru.raysmith.tgbot.model.api.inline.SentWebAppMessage
import ru.raysmith.tgbot.model.api.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.api.inline.result.InlineQueryResultsButton
import ru.raysmith.tgbot.model.api.keyboard.InlineKeyboardButton
import ru.raysmith.tgbot.model.api.keyboard.KeyboardButton
import ru.raysmith.tgbot.model.api.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.api.media.Story
import ru.raysmith.tgbot.model.api.media.input.*
import ru.raysmith.tgbot.model.api.menubutton.MenuButton
import ru.raysmith.tgbot.model.api.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.api.message.*
import ru.raysmith.tgbot.model.api.message.checklist.InputChecklist
import ru.raysmith.tgbot.model.api.message.reaction.ReactionType
import ru.raysmith.tgbot.model.api.message.rich.InputRichMessage
import ru.raysmith.tgbot.model.api.payment.LabeledPrice
import ru.raysmith.tgbot.model.api.payment.ShippingOption
import ru.raysmith.tgbot.model.api.payment.stars.StarTransactions
import ru.raysmith.tgbot.model.api.poll.Poll
import ru.raysmith.tgbot.model.api.profile.InputProfilePhoto
import ru.raysmith.tgbot.model.api.response.Error
import ru.raysmith.tgbot.model.api.response.LiveLocationResponse
import ru.raysmith.tgbot.model.api.response.NetworkResponse
import ru.raysmith.tgbot.model.api.sticker.*
import ru.raysmith.tgbot.model.api.story.InputStoryContent
import ru.raysmith.tgbot.model.api.story.StoryArea
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters
import ru.raysmith.tgbot.model.api.updates.Update
import ru.raysmith.tgbot.model.api.updates.UpdateType
import ru.raysmith.tgbot.model.api.updates.boost.UserChatBoosts
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.user.UserProfileAudios
import ru.raysmith.tgbot.model.api.user.UserProfilePhotos
import ru.raysmith.tgbot.model.bot.*
import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.LivePeriod
import ru.raysmith.tgbot.model.bot.message.poll.InputPollOption
import java.io.InputStream
import java.time.ZonedDateTime
import javax.swing.UIManager.put
import kotlin.time.Duration

// TODO missed docs + check alt. method in BotContext

interface API {
    val botConfig: BotConfig // TODO make internal
    val client: HttpClient // TODO make internal

    private suspend inline fun <reified T> request(block: () -> HttpResponse): T {
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
            throw BadBotTokenException("Bot is unauthorized with token")
        } else {
            val error = response.body<Error>()
            if (error.errorCode == 404 && response.request.url.rawSegments.lastOrNull() == "getUpdates") {
                val token = response.request.url.rawSegments.firstOrNull { it.startsWith("bot") }?.drop(3)
                throw BadBotTokenException("The bot token '$token' is incorrect or have been revoked")
            }
            throw TelegramApiException(error, response.request)
        }
    }

    /**
     * Use this method to receive incoming updates using long polling
     * ([wiki](https://en.wikipedia.org/wiki/Push_technology#Long_polling)).
     *
     * @param offset Identifier of the first update to be returned. Must be greater by one than the highest among the
     * identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update
     * are returned. An update is considered confirmed as soon as [getUpdates] is called with an *offset* higher than
     * its *update_id*. The negative offset can be specified to retrieve updates starting from -*offset* update from
     * the end of the updates queue. All previous updates will be forgotten.
     * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive,
     * short polling should be used for testing purposes only.
     * @param allowedUpdates A list of the update types you want your bot to receive. For example, specify
     * [[UpdateType.MESSAGE], [UpdateType.EDITED_CHANNEL_POST], [UpdateType.CALLBACK_QUERY]] to only receive updates
     * of these types. Specify an empty list to receive all update types except [UpdateType.CHAT_MEMBER],
     * [UpdateType.MESSAGE_REACTION] and [UpdateType.MESSAGE_REACTION_COUNT] (default).
     * If not specified, the previous setting will be used.
     *
     * > Please note that this parameter doesn't affect updates created before the call to [getUpdates],
     * so unwanted updates may be received for a short period of time.
     * */
    suspend fun getUpdates(
        offset: Int? = null,
        limit: Int? = null,
        timeout: Int? = null,
        allowedUpdates: List<UpdateType>? = null,
    ) = request<List<Update>> {
        client.post("getUpdates") {
            setBody(buildJsonObject {
                put("offset", offset)
                put("limit", limit)
                put("timeout", timeout)
                putJsonArray("allowed_updates") {
                    allowedUpdates?.forEach {
                        add(TelegramApi.json.encodeToJsonElement(it))
                    }
                }
            })

//            setFormDataBody {
//                appendSerializable("offset", offset)
//                appendSerializable("limit", limit)
//                appendSerializable("timeout", timeout)
//                appendSerializable("allowed_updates", allowedUpdates)
//            }
        }
    }

    /**
     * Use this method to specify a URL and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL,
     * containing a JSON-serialized [Update]. In case of an unsuccessful request (a request with response
     * [HTTP status code](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) different from `2XY`),
     * we will repeat the request and give up after a reasonable amount of attempts. Returns *True* on Success.
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
     * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration.
     * @param certificate Upload your public key certificate so that the root certificate in use can be checked.
     * See our [self-signed](https://core.telegram.org/bots/self-signed) guide for details.
     * @param ipAddress The fixed IP address which will be used to send webhook requests
     * instead of the IP address resolved through DNS
     * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update
     * delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot's server, and higher
     * values to increase your bot's throughput.
     * @param allowedUpdates A list of the update types you want your bot to receive. For example, specify
     * [[UpdateType.MESSAGE], [UpdateType.EDITED_CHANNEL_POST], [UpdateType.CALLBACK_QUERY]] to only receive updates
     * of these types. Specify an empty list to receive all update types except [UpdateType.CHAT_MEMBER],
     * [UpdateType.MESSAGE_REACTION] and [UpdateType.MESSAGE_REACTION_COUNT] (default).
     * If not specified, the previous setting will be used.
     *
     * > Please note that this parameter doesn't affect updates created before the call to the [getUpdates],
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
        allowedUpdates: List<UpdateType>? = null,
        dropPendingUpdates: Boolean? = null,
        secretToken: String? = null,
    ) = request<Boolean> {
        client.post("setWebhook") {
            parameter("url", url)

            setFormDataBody {
                appendSerializable("certificate", certificate)
                appendSerializable("ip_address", ipAddress)
                appendSerializable("max_connections", maxConnections)
                appendSerializable("allowed_updates", allowedUpdates)
                appendSerializable("drop_pending_updates", dropPendingUpdates)
                appendSerializable("secret_token", secretToken)
            }
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
     * Returns basic information about the bot in form of a [ru.raysmith.tgbot.model.api.user.User] object.
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
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required
     *  if the message is sent to a direct messages chat
     * @param text Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode Mode  for parsing entities in the message text.
     * @param entities List of special entities that appear in message text, which can be specified instead of
     *  [parseMode]
     * @param linkPreviewOptions Link preview generation options for the message
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send; for direct
     *  messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is
     *  automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. Object for an
     *  [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     *  [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to
     *  force a reply from the user.
     * */
    suspend fun sendMessage(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        text: String,
        parseMode: ParseMode? = null,
        entities: List<MessageEntity>? = null,
        linkPreviewOptions: LinkPreviewOptions? = botConfig.linkPreviewOptions,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ): Message = request<Message> {
        client.post("sendMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("text", text)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("entities", entities)
                appendSerializable("link_preview_options", linkPreviewOptions)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to forward messages of any kind. Service messages and messages with protected content can't be
     * forwarded. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be forwarded;
     *  required if the message is forwarded to a direct messages chat
     * @param fromChatId Unique identifier for the chat where the original message was sent (or username of the targe
     *  bot, supergroup or channel)
     * @param videoStartTimestamp New start timestamp for the forwarded video in the message
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message; only available when
     *  forwarding to private chats
     * @param suggestedPostParameters
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * */
    suspend fun forwardMessage(
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        fromChatId: ChatId,
        videoStartTimestamp: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        messageId: Int,
    ) = request<Message> {
        client.post("forwardMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("from_chat_id", fromChatId)
                appendSerializable("video_start_timestamp", videoStartTimestamp)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("message_id", messageId)
            }
        }
    }

    /**
     * Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or
     * forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album
     * grouping is kept for forwarded messages. On success, the sent [Message] is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be forwarded;
     *  required if the messages are forwarded to a direct messages chat
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or username of the target
     *  bot, supergroup or channel in the format)
     * @param messageIds A list of 1-100 identifiers of messages in the chat [fromChatId] to forward. The identifiers
     *  must be specified in a strictly increasing order.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the forwarded message from forwarding and saving
     * */
    suspend fun forwardMessages(
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        fromChatId: ChatId,
        messageIds: List<Int>,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
    ) = request<List<MessageId>> {
        client.post("forwardMessages") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("from_chat_id", fromChatId)
                appendSerializable("message_ids", messageIds)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
            }
        }
    }

    /**
     * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway
     * winners messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz
     * [poll](https://core.telegram.org/bots/api#poll) can be copied only if the value of the field
     * [correctOptionId][Poll.correctOptionIds] is known to the bot. The method is analogous to the method
     * [forwardMessage], but the copied message doesn't have a link to the original message. Returns the [MessageId] of
     * the sent message on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required
     *  if the messages are sent to a direct messages chat
     * @param fromChatId Unique identifier for the chat where the original message was sent
     * @param messageId Message identifier in the chat specified in [fromChatId]
     * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original
     *  caption is kept
     * @param parseMode Mode  for parsing entities in the message caption.
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of
     *  [parseMode]
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media. Ignored if a new
     *  caption isn't specified.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. Object for an
     *  [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     *  [custom reply keyboard](https://core.telegram.org/bots#keyboards), instructions to remove reply keyboard or to
     *  force a reply from the user.
     * */
    suspend fun copyMessage(
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        fromChatId: ChatId,
        messageId: Int,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<MessageId> {
        client.post("copyMessage") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("from_chat_id", fromChatId)
                appendSerializable("message_id", messageId)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they
     * are skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice
     * messages can't be copied. A quiz poll can be copied only if the value of the field
     * [correctOptionId][Poll.correctOptionIds] is known to the bot. The method is analogous to the method
     * [forwardMessages], but the copied messages don't have a link to the original message. Album grouping is kept for
     * copied messages. On success, an array of [MessageId] of the sent messages is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required
     *  if the messages are sent to a direct messages chat
     * @param fromChatId Unique identifier for the chat where the original messages were sent (or username of the target
     *  bot, supergroup or channel)
     * @param messageIds A list of 1-100 identifiers of messages in the chat [fromChatId] to copy. The identifiers must
     *  be specified in a strictly increasing order.
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param removeCaption Pass *True* to copy the messages without their captions
     * */
    suspend fun copyMessages(
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        fromChatId: ChatId,
        messageIds: List<Int>,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        removeCaption: Boolean? = null,
    ) = request<List<MessageId>> {
        client.post("copyMessages") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("from_chat_id", fromChatId)
                appendSerializable("message_ids", messageIds)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("removeCaption", removeCaption)
            }

        }
    }

    /**
     * Use this method to send photos
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message
     *  will be sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum;
     *  for forum supergroups and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent;
     *  required if the message is sent to a direct messages chat
     * @param photo Photo to send. Pass an [InputFile.FileIdOrUrl] with file id to send a photo that exists on the
     *  Telegram servers (recommended), pass an [InputFile.FileIdOrUrl] with HTTP URL for Telegram to get a photo from
     *  the Internet, or upload a new photo using [InputFile.File]. The photo must be at most 10 MB in size.
     *  The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20.
     *  [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * @param caption Photo caption (may also be used when resending photos by [InputFile.FileIdOrUrl]),
     *  0-1024 characters after entities parsing
     * @param parseMode Mode  for parsing entities in the photo caption.
     * @param captionEntities List of special entities that appear in message text, which can be specified instead of
     *  *[parseMode]*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
     * @param hasSpoiler Pass *True* if the photo needs to be covered with a spoiler animation
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send;
     *  for direct messages chats only.
     *  If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. Object for an
     *  [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     *  [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     *  instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun sendPhoto(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        photo: InputFile,
        caption: String? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendPhoto") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("photo", photo)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("has_spoiler", hasSpoiler)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to send live photos
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will
     *  be sent
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum
     *  supergroups and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent;
     *  required if the message is sent to a direct messages chat
     * @param livePhoto Live photo video to send. The video must be no longer than 10 seconds and must not exceed 10 MB
     *  in size. Pass a [InputFile.FileIdOrUrl] to send a video that exists on the Telegram servers
     *  (recommended) or upload a new video using [InputFile.File] or [InputFile.ByteArray].
     *  [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files).
     *  Sending live photos by a URL is currently unsupported.
     * @param photo The static photo to send. Pass a [InputFile.FileIdOrUrl] to send a photo that exists on the Telegram
     *  servers (recommended) or upload a new video using multipart/form-data.
     *  [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files).
     *  Sending live photos by a URL is currently unsupported.
     * @param caption Video caption (may also be used when resending videos by [InputFile.FileIdOrUrl]), 0-1024
     *  characters after entities parsing
     * @param parseMode Mode for parsing entities in the video caption
     * @param captionEntities List of special entities that appear in the caption, which can be specified instead of
     *  *[parseMode]*
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
     * @param hasSpoiler Pass *True* if the video needs to be covered with a spoiler animation
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats
     *  only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send;
     *  for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested
     *  post is automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. Object for an
     *  [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     *  [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     *  instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun sendLivePhoto(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        livePhoto: InputFile,
        photo: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Any> {
        client.post("sendLivePhoto") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("live_photo", livePhoto)
                appendSerializable("photo", photo)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("has_spoiler", hasSpoiler)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }

        }
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player.
     * Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned.
     * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message
     * will be sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum;
     * for forum supergroups and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent;
     *  required if the message is sent to a direct messages chat
     * @param audio Audio file to send
     * @param caption Audio caption, 0-1024 characters after entities parsing
     * @param parseMode Mode  for parsing entities in the audio caption.
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
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send; for direct
     *  messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is
     *  automatically declined.
     *  If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. Object for an
     * [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards),
     * [custom reply keyboard](https://core.telegram.org/bots#keyboards),
     * instructions to remove reply keyboard or to force a reply from the user.
     * */
    suspend fun sendAudio(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        audio: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null,
        thumbnail: NotReusableInputFile? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendAudio") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("audio", audio)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("duration", duration)
                appendSerializable("performer", performer)
                appendSerializable("title", title)
                appendSerializable("thumbnail", thumbnail)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("protectContent", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    suspend fun sendDocument(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        document: InputFile,
        thumbnail: NotReusableInputFile?,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        disableContentTypeDetection: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendDocument") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("document", document)
                appendSerializable("thumbnail", thumbnail)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("disable_content_type_detection", disableContentTypeDetection)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendVideo(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        video: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumbnail: NotReusableInputFile? = null,
        cover: InputFile? = null,
        startTimestamp: Int? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        supportsStreaming: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVideo") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("video", video)
                appendSerializable("thumbnail", thumbnail)
                appendSerializable("cover", cover)
                appendSerializable("duration", duration)
                appendSerializable("width", width)
                appendSerializable("height", height)
                appendSerializable("start_timestamp", startTimestamp)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("has_spoiler", hasSpoiler)
                appendSerializable("supports_streaming", supportsStreaming)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendAnimation(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        animation: InputFile,
        duration: Int? = null,
        width: Int? = null,
        height: Int? = null,
        thumbnail: NotReusableInputFile? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        hasSpoiler: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendAnimation") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("animation", animation)
                appendSerializable("thumbnail", thumbnail)
                appendSerializable("duration", duration)
                appendSerializable("width", width)
                appendSerializable("height", height)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("has_spoiler", hasSpoiler)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }

        }
    }

    suspend fun sendVoice(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        voice: InputFile,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        duration: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVoice") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("voice", voice)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("duration", duration)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendVideoNote(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        videoNote: InputFile,
        duration: Int? = null,
        length: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVideoNote") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("video_note", videoNote)
                appendSerializable("duration", duration)
                appendSerializable("length", length)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    // TODO docs
    /**
     * Use this method to send paid media. On success, the sent [Message] is returned.
     * */
    suspend fun sendPaidMedia(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        starCount: Int,
        media: List<InputPaidMedia>,
        payload: String? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendPaidMedia") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("star_count", starCount)
                appendSerializable("media", media)
                appendSerializable("caption", caption)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    /**
     * Use this method to send a group of photos, live photos, videos, documents or audios as an album.
     * Documents and audio files can be only grouped in an album with messages of the same type.
     * On success, an array of [Message] objects that were sent is returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum;
     *  for forum supergroups and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent;
     *  required if the message is sent to a direct messages chat
     * @param media An array describing messages to be sent, must include 2-10 items
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent messages from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param replyParameters Description of the message to reply to
     * */
    suspend fun sendMediaGroup(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        media: List<InputMediaGroup>,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
    ) = request<List<Message>> {
        client.post("sendMediaGroup") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("media", media as List<InputMedia>)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("reply_parameters", replyParameters)
            }
        }
    }

    suspend fun sendLocation(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        latitude: Double,
        longitude: Double,
        horizontalAccuracy: Double? = null,
        livePeriod: LivePeriod? = null,
        heading: Int? = null,
        proximityAlertRadius: Int? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendLocation") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("latitude", latitude)
                appendSerializable("longitude", longitude)
                appendSerializable("horizontal_accuracy", horizontalAccuracy)
                appendSerializable("live_period", livePeriod)
                appendSerializable("heading", heading)
                appendSerializable("proximity_alert_radius", proximityAlertRadius)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendVenue(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
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
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendVenue") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("latitude", latitude)
                appendSerializable("longitude", longitude)
                appendSerializable("title", title)
                appendSerializable("address", address)
                appendSerializable("foursquare_id", foursquareId)
                appendSerializable("foursquare_type", foursquareType)
                appendSerializable("google_place_id", googlePlaceId)
                appendSerializable("google_place_type", googlePlaceType)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendContact(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        phoneNumber: String,
        firstName: String,
        lastName: String? = null,
        vcard: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendContact") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("phone_number", phoneNumber)
                appendSerializable("first_name", firstName)
                appendSerializable("last_name", lastName)
                appendSerializable("vcard", vcard)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun sendPoll(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        question: String,
        questionParseMode: ParseMode? = null,
        questionEntities: List<MessageEntity>? = null,
        options: List<InputPollOption>,
        isAnonymous: Boolean? = null,
        type: PollType? = null,
        allowsMultipleAnswers: Boolean? = null,
        allowsRevoting: Boolean? = null,
        shuffleOptions: Boolean? = null,
        allowAddingOptions: Boolean? = null,
        hideResultsUntilCloses: Boolean? = null,
        membersOnly: Boolean? = null,
        countryCodes: List<String>? = null,
        correctOptionIds: List<Int>? = null,
        explanation: String? = null,
        explanationParseMode: ParseMode? = null,
        explanationEntities: List<MessageEntity>? = null,
        explanationMedia: InputPollMedia? = null,
        openPeriod: Int? = null,
        closeDate: Int? = null,
        isClosed: Boolean? = null,
        description: String? = null,
        descriptionParseMode: ParseMode? = null,
        descriptionEntities: List<MessageEntity>? = null,
        media: InputPollMedia? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendPoll") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)

            setFormDataBody {
                appendSerializable("question", question)
                appendSerializable("question_parse_mode", questionParseMode)
                appendSerializable("question_entities", questionEntities)
                appendSerializable("options", options)
                appendSerializable("is_anonymous", isAnonymous)
                appendSerializable("type", type)
                appendSerializable("allows_multiple_answers", allowsMultipleAnswers)
                appendSerializable("allows_revoting", allowsRevoting)
                appendSerializable("shuffle_options", shuffleOptions)
                appendSerializable("allow_adding_options", allowAddingOptions)
                appendSerializable("hide_results_until_closes", hideResultsUntilCloses)
                appendSerializable("members_only", membersOnly)
                appendSerializable("country_codes", countryCodes)
                appendSerializable("correct_option_ids", correctOptionIds)
                appendSerializable("explanation", explanation)
                appendSerializable("explanation_parse_mode", explanationParseMode)
                appendSerializable("explanation_entities", explanationEntities)
                appendSerializable("explanation_media", explanationMedia)
                appendSerializable("open_period", openPeriod)
                appendSerializable("close_date", closeDate)
                appendSerializable("is_closed", isClosed)
                appendSerializable("description", description)
                appendSerializable("description_parse_mode", descriptionParseMode)
                appendSerializable("description_entities", descriptionEntities)
                appendSerializable("media", media)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    /**
     * Use this method to send a checklist on behalf of a connected business account. On success, the sent Message is
     * returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param chatId Unique identifier for the target chat or username of the target bot in the format @username
     * @param checklist A JSON-serialized object for the checklist to send
     * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param messageEffectId Unique identifier of the message effect to be added to the message
     * @param replyParameters A JSON-serialized object for description of the message to reply to
     * @param replyMarkup A JSON-serialized object for an inline keyboard
     * */
    suspend fun sendChecklist(
        businessConnectionId: String,
        chatId: ChatId,
        checklist: InputChecklist,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Any> {
        client.post("sendChecklist") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("checklist", checklist)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }

        }
    }

    suspend fun sendDice(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        emoji: String,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendDice") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("emoji", emoji)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    /**
     * Use this method to stream a partial message to a user while the message is being generated. Note that the
     * streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you must
     * call sendMessage with the complete message to persist it in the user's chat. Returns True on success.
     *
     * @param chatId Unique identifier for the target private chat
     * @param messageThreadId Unique identifier for the target message thread
     * @param draftId Unique identifier of the message draft; must be non-zero.
     *  Changes to drafts with the same identifier are animated.
     * @param text Text of the message to be sent, 0-4096 characters after entities parsing.
     *  Pass an empty text to show a “Thinking…” placeholder.
     * @param parseMode Mode for parsing entities in the message text. See [formatting options][ParseMode] for more
     *  details.
     * @param entities List of special entities that appear in message text, which can be specified instead of
     *  [parseMode]
     * */
    suspend fun sendMessageDraft(
        chatId: ChatId,
        messageThreadId: Int? = null,
        draftId: Int,
        text: String? = null,
        parseMode: ParseMode? = null,
        entities: List<MessageEntity>? = null,
    ) = request<Boolean> {
        client.post("sendMessageDraft") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)

            setFormDataBody {
                appendSerializable("draft_id", draftId)
                appendSerializable("text", text)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("entities", entities)
            }
        }
    }

    /**
     * Use this method when you need to tell the user that something is happening on the bot's side.The status is set
     * for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status).
     * Returns True on success.
     *
     * > Example: The [ImageBot](https://t.me/imagebot) needs some time to process a request and upload the image.
     * Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use
     * [sendChatAction] with action = [ChatAction.UPLOAD_PHOTO]. The user will see a “sending photo” status for the bot.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the action
     * will be sent
     * @param chatId Unique identifier for the target chat or username of the target bot or supergroup.
     *  Channel chats and channel direct messages chats aren't supported.
     * @param messageThreadId Unique identifier for the target message thread or topic of a forum;
     *  for supergroups and private chats of bots with forum topic mode enabled only
     * @param action Type of action to broadcast. Choose one, depending on what the user is about to receive:
     * - [ChatAction.TYPING] for text messages,
     * - [ChatAction.UPLOAD_PHOTO] for photos,
     * - [ChatAction.RECORD_VIDEO] or [ChatAction.UPLOAD_VIDEO] for videos,
     * - [ChatAction.RECORD_VOICE] or [ChatAction.UPLOAD_VOICE] for voice notes,
     * - [ChatAction.UPLOAD_DOCUMENT] for general files,
     * - [ChatAction.CHOOSE_STICKER] for stickers,
     * - [ChatAction.FIND_LOCATION] for location data,
     * - [ChatAction.RECORD_VIDEO_NOTE] or [ChatAction.UPLOAD_VIDEO_NOTE] for video notes.
     * */
    suspend fun sendChatAction(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        action: ChatAction,
    ) = request<Boolean> {
        client.post("sendChatAction") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)

            setFormDataBody {
                appendSerializable("action", action)
            }
        }
    }

    /**
     * Use this method to change the chosen reactions on a message. Service messages of some types can't be reacted to.
     * Automatically forwarded messages from a channel to its discussion group have the same available reactions as
     * messages in the channel. Bots can't use paid reactions. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageId Identifier of the target message. If the message belongs to a media group, the reaction is set
     *  to the first non-deleted message in the group instead.
     * @param reaction A list of reaction types to set on the message. Currently, as non-premium users, bots can set up
     *  to one reaction per message. A custom emoji reaction can be used if it is either already present on the message
     *  or explicitly allowed by chat administrators. Paid reactions can't be used by bots.
     * @param isBig Pass *True* to set the reaction with a big animation
     * */
    suspend fun setMessageReaction(
        chatId: ChatId,
        messageId: Int,
        reaction: List<ReactionType>,
        isBig: Boolean,
    ) = request<Boolean> {
        client.post("setMessageReaction") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("reaction", reaction)
                appendSerializable("is_big", isBig)
            }
        }
    }

    /**
     * Use this method to get a list of profile pictures for a user.
     *
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
            setFormDataBody {
                parameter("user_id", userId)
                parameter("offset", offset)
                parameter("limit", limit)
            }
        }
    }

    /**
     * Use this method to get a list of profile audios for a user. Returns a UserProfileAudios object.
     *
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first audio to be returned. By default, all audios are returned.
     * @param limit Limits the number of audios to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * */
    suspend fun getUserProfileAudios(
        userId: Int,
        offset: Int? = null,
        limit: Int? = null,
    ) = request<UserProfileAudios> {
        client.post("getUserProfileAudios") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("offset", offset)
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the
     * Mini App method `requestEmojiStatusAccess`. Returns *True* on success.
     * @param userId Unique identifier of the target user
     * @param emojiStatusCustomEmojiId Custom emoji identifier of the emoji status to set. Pass an empty string to
     * remove the status.
     * @param emojiStatusExpirationDate Expiration date of the emoji status, if any
     * */
    suspend fun setUserEmojiStatus(
        userId: ChatId.ID,
        emojiStatusCustomEmojiId: String? = null,
        emojiStatusExpirationDate: Int? = null,
    ) = request<Boolean> {
        client.post("setUserEmojiStatus") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("emoji_status_custom_emoji_id", emojiStatusCustomEmojiId)
                appendSerializable("emoji_status_expiration_date", emojiStatusExpirationDate)
            }
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
     * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels,the
     * user will not be able to return to the chat on their own using invite links, etc., unless
     * [unbanned][unbanChatMember] first. The bot must be an administrator in the chat for this to work and must have
     * the appropriate admin rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned. If user is banned for more than 366 days or less than 30
     *  seconds from the current time they are considered to be banned forever. Applied for supergroups and channels
     *  only.
     * @param revokeMessages Pass *True* to delete all messages from the chat for the user that is being removed. If
     *  *False*, the user will be able to see messages in the group that were sent before the user was removed. Always
     *  *True* for supergroups and channels.
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

            setFormDataBody {
                appendSerializable("until_date", untilDate)
                appendSerializable("revoke_messages", revokeMessages)
            }
        }
    }

    /**
     * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the
     * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this
     * to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be
     * able to join it. So if the user is a member of the chat they will also be **removed** from the chat. If you don't
     * want this, use the parameter [onlyIfBanned]. Returns *True* on success.
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

            setFormDataBody {
                appendSerializable("only_if_banned", onlyIfBanned)
            }
        }
    }

    /**
     * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this
     * to work and must have the appropriate administrator rights. Pass *True* for all permissions to lift restrictions
     * from a user. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param userId Unique identifier of the target user
     * @param permissions New user permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the
     *  [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews] permissions will imply the
     *  [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios], [ChatPermissions.canSendDocuments],
     *  [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos], [ChatPermissions.canSendVideoNotes], and
     *  [ChatPermissions.canSendVoiceNotes] permissions; the [ChatPermissions.canSendPolls] permission will imply the
     *  [ChatPermissions.canSendMessages] permission.
     * @param untilDate Date when restrictions will be lifted for the user. If user is restricted for more than 366 days
     *  or less than 30 seconds from the current time, they are considered to be restricted forever.
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

            setFormDataBody {
                appendSerializable("permissions", permissions)
                appendSerializable("use_independent_chat_permissions", useIndependentChatPermissions)
                appendSerializable("until_date", untilDate)
            }
        }
    }

    /**
     * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the
     * chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters
     * to demote a user. Returns True on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel in the format @username
     * @param userId Unique identifier of the target user
     * @param isAnonymous Pass *True* if the administrator's presence in the chat is hidden
     * @param canManageChat Pass *True* if the administrator can access the chat event log, get boost list, see hidden
     *  supergroup and channel members, report spam messages, ignore slow mode, and send messages to the chat without
     *  paying Telegram Stars. Implied by any other administrator privilege.
     * @param canDeleteMessages Pass *True* if the administrator can delete messages of other users
     * @param canManageVideoChats Pass *True* if the administrator can manage video chats
     * @param canRestrictMembers Pass *True* if the administrator can restrict, ban or unban chat members, or access
     *  supergroup statistics. For backward compatibility, defaults to True for promotions of channel administrators.
     * @param canPromoteMembers Pass *True* if the administrator can add new administrators with a subset of their own
     *  privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators
     *  that were appointed by him)
     * @param canChangeInfo Pass *True* if the administrator can change chat title, photo and other settings
     * @param canInviteUsers Pass *True* if the administrator can invite new users to the chat
     * @param canPostStories Pass *True* if the administrator can post stories to the chat
     * @param canEditStories Pass *True* if the administrator can edit stories posted by other users, post stories to the
     *  chat page, pin chat stories, and access the chat's story archive
     * @param canDeleteStories Pass *True* if the administrator can delete stories posted by other users
     * @param canPostMessages Pass *True* if the administrator can post messages in the channel, approve suggested posts,
     *  or access channel statistics; for channels only
     * @param canEditMessages Pass *True* if the administrator can edit messages of other users and can pin messages; for
     *  channels only
     * @param canPinMessages Pass *True* if the administrator can pin messages; for supergroups only
     * @param canManageTopics Pass *True* if the user is allowed to create, rename, close, and reopen forum topics; for
     *  supergroups only
     * @param canManageDirectMessages Pass *True* if the administrator can manage direct messages within the channel and
     *  decline suggested posts; for channels only
     * @param canManageTags Pass *True* if the administrator can edit the tags of regular members; for groups and
     *  supergroups only
     * */
    suspend fun promoteChatMember(
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
        canManageDirectMessages: Boolean? = null,
        canManageTags: Boolean? = null,
    ) = request<Boolean> {
        client.post("promoteChatMember") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("is_anonymous", isAnonymous)
                appendSerializable("can_manage_chat", canManageChat)
                appendSerializable("can_post_messages", canPostMessages)
                appendSerializable("can_edit_messages", canEditMessages)
                appendSerializable("can_delete_messages", canDeleteMessages)
                appendSerializable("can_post_stories", canPostStories)
                appendSerializable("can_edit_stories", canEditStories)
                appendSerializable("can_delete_stories", canDeleteStories)
                appendSerializable("can_manage_video_chats", canManageVideoChats)
                appendSerializable("can_restrict_members", canRestrictMembers)
                appendSerializable("can_promote_members", canPromoteMembers)
                appendSerializable("can_change_info", canChangeInfo)
                appendSerializable("can_invite_users", canInviteUsers)
                appendSerializable("can_pin_messages", canPinMessages)
                appendSerializable("can_manage_topics", canManageTopics)
                appendSerializable("can_manage_direct_messages", canManageDirectMessages)
                appendSerializable("can_manage_tags", canManageTags)
            }
        }
    }

    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns *True* on
     *  success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param userId Unique identifier of the target user
     * @param customTitle Unique identifier of the target user; 0-16 characters, emoji are not allowed
     * */
    suspend fun setChatAdministratorCustomTitle(
        chatId: ChatId,
        userId: ChatId.ID,
        customTitle: String,
    ) = request<Boolean> {
        client.post("setChatAdministratorCustomTitle") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("custom_title", customTitle)
            }
        }
    }

    /**
     * Use this method to set a tag for a regular member in a group or a supergroup. The bot must be an administrator in
     * the chat for this to work and must have the [canManageTags][ChatMemberAdministrator.canManageTags] administrator
     * right. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param userId Unique identifier of the target user
     * @param tag New tag for the member; 0-16 characters, emoji are not allowed
     * */
    suspend fun setChatMemberTag(
        chatId: ChatId,
        userId: Int,
        tag: String? = null,
    ) = request<Boolean> {
        client.post("setChatMemberTag") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("tag", tag)
            }
        }
    }

    /**
     * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is
     * [unbanned][unbanChatSenderChat], the owner of the banned chat won't be able to send messages on behalf of **any
     * of their channels**. The bot must be an administrator in the supergroup or channel for this to work and must have
     * the appropriate administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param senderChatId Unique identifier of the target sender chat
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
     * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an
     * administrator for this to work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param senderChatId Unique identifier of the target sender chat
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
     * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or
     * a supergroup for this to work and must have the [canRestrictMembers][ChatAdministratorRights.canRestrictMembers]
     * administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param permissions New default chat permissions
     * @param useIndependentChatPermissions Pass *True* if chat permissions are set independently. Otherwise, the
     *  [ChatPermissions.canSendOtherMessages] and [ChatPermissions.canAddWebPagePreviews] permissions will imply the
     *  [ChatPermissions.canSendMessages], [ChatPermissions.canSendAudios], [ChatPermissions.canSendDocuments],
     *  [ChatPermissions.canSendPhotos], [ChatPermissions.canSendVideos], [ChatPermissions.canSendVideoNotes], and
     *  [ChatPermissions.canSendVoiceNotes] permissions; the [ChatPermissions.canSendPolls] permission will imply the
     *  [ChatPermissions.canSendMessages] permission.
     * */
    suspend fun setChatPermissions(
        chatId: ChatId,
        permissions: ChatPermissions,
        useIndependentChatPermissions: Boolean? = null,
    ) = request<Boolean> {
        client.post("setChatPermissions") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("permissions", permissions)
                appendSerializable("use_independent_chat_permissions", useIndependentChatPermissions)
            }
        }
    }

    /**
     * Use this method to generate a new primary invite link for a chat; any previously generated primary link is
     * revoked. The bot must be an administrator in the chat for this to work and must have the appropriate
     * administrator rights. Returns the new invite link as String on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
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
     * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for
     * this to work and must have the appropriate administrator rights. The link can be revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the
     *  chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     *  administrators. If *True*, [memberLimit] can't be specified.
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

            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("expire_date", expireDate?.toEpochSecond())
                appendSerializable("member_limit", memberLimit)
                appendSerializable("creates_join_request", createsJoinRequest)
            }
        }
    }

    /**
     * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the
     * chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a
     * [ChatInviteLink] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * @param expireDate Date when the link will expire
     * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the
     *  chat via this invite link; 1-99999
     * @param createsJoinRequest *True*, if users joining the chat via the link need to be approved by chat
     *  administrators. If *True*, [memberLimit] can't be specified
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

            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("expire_date", expireDate?.toEpochSecond())
                appendSerializable("member_limit", memberLimit)
                appendSerializable("creates_join_request", createsJoinRequest)
            }
        }
    }

    /**
     * Use this method to create a
     * [subscription invite link](https://telegram.org/blog/superchannels-star-reactions-subscriptions#star-subscriptions)
     * for a channel chat. The bot must have the [canInviteUsers][ChatAdministratorRights.canInviteUsers] administrator
     * rights. The link can be edited using the method [editChatSubscriptionInviteLink] or revoked using the method
     * [revokeChatInviteLink]. Returns the new invite link as a [ChatInviteLink] object.
     *
     * @param chatId Unique identifier for the target channel chat or username of the target channel
     * @param name Invite link name; 0-32 characters
     * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment.
     *  Currently, it must always be 2592000 (30 days).
     * @param subscriptionPrice The amount of Telegram Stars a user must pay initially and after each subsequent
     *  subscription period to be a member of the chat; 1-10000
     * */
    suspend fun createChatSubscriptionInviteLink(
        chatId: ChatId,
        name: String? = null,
        subscriptionPeriod: Int,
        subscriptionPrice: Int,
    ) = request<ChatInviteLink> {
        client.post("createChatSubscriptionInviteLink") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("subscription_period", subscriptionPeriod)
                appendSerializable("subscription_price", subscriptionPrice)
            }
        }
    }

    /**
     * Use this method to edit a subscription invite link created by the bot. The bot must have the
     * [ChatAdministratorRights.canInviteUsers] administrator rights. Returns the edited invite link as a
     * [ChatInviteLink] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param inviteLink The invite link to edit
     * @param name Invite link name; 0-32 characters
     * */
    suspend fun editChatSubscriptionInviteLink(
        chatId: ChatId,
        inviteLink: String,
        name: String? = null,
    ) = request<ChatInviteLink> {
        client.post("editChatSubscriptionInviteLink") {
            parameter("chat_id", chatId)
            parameter("invite_link", inviteLink)

            setFormDataBody {
                parameter("name", name)
            }
        }
    }

    /**
     * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
     * automatically generated. The bot must be an administrator in the chat for this to work and must have the
     * appropriate administrator rights. Returns the revoked invite link as [ChatInviteLink] object.
     *
     * @param chatId Unique identifier of the target chat or username of the target channel
     * @param inviteLink The invite link to revoke
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
     * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and
     * must have the [canInviteUsers][ChatAdministratorRights.canInviteUsers] administrator right. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param userId Unique identifier of the target user
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
     * and must have the [canInviteUsers][ChatAdministratorRights.canInviteUsers] administrator right. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param userId Unique identifier of the target user
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
     * Use this method to process a received chat join request query. Returns True on success.
     *
     * @param chatJoinRequestQueryId Unique identifier of the join request query
     * @param result Result of the query. Must be either “approve” to allow the user to join the chat, “decline” to
     *  disallow the user to join the chat, or “queue” to leave the decision to other administrators.
     * */
    suspend fun answerChatJoinRequestQuery(
        chatJoinRequestQueryId: String,
        result: String,
    ) = request<Boolean> {
        client.post("answerChatJoinRequestQuery") {
            setFormDataBody {
                appendSerializable("chat_join_request_query_id", chatJoinRequestQueryId)
                appendSerializable("result", result)
            }
        }
    }

    /**
     * Use this method to process a received chat join request query by showing a Mini App to the user before deciding
     * the outcome. Returns *True* on success.
     *
     * @param chatJoinRequestQueryId Unique identifier of the join request query
     * @param webAppUrl The URL of the Mini App to be opened
     * */
    suspend fun sendChatJoinRequestWebApp(
        chatJoinRequestQueryId: String,
        webAppUrl: String,
    ) = request<Boolean> {
        client.post("sendChatJoinRequestWebApp") {
            setFormDataBody {
                appendSerializable("chat_join_request_query_id", chatJoinRequestQueryId)
                appendSerializable("web_app_url", webAppUrl)
            }
        }
    }

    /**
     * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must
     * be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns
     * *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param photo New chat photo
     * */
    suspend fun setChatPhoto(
        chatId: ChatId,
        photo: NotReusableInputFile,
    ) = request<Boolean> {
        client.post("setChatPhoto") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("photo", photo)
            }
        }
    }

    /**
     * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on
     * success.
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
     * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an
     * administrator in the chat for this to work and must have the appropriate administrator rights. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param title New chat title, 1-255 characters
     * */
    suspend fun setChatTitle(
        chatId: ChatId,
        title: String,
    ) = request<Boolean> {
        client.post("setChatTitle") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("title", title)
            }
        }
    }

    /**
     * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator
     * in the chat for this to work and must have the appropriate administrator rights. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param description New chat description, 0-255 characters
     * */
    suspend fun setChatDescription(
        chatId: ChatId,
        description: String?,
    ) = request<Boolean> {
        client.post("setChatDescription") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("description", description)
            }
        }
    }

    /**
     * Use this method to add a message to the list of pinned messages in a chat. In private chats and channel direct
     * messages chats, all non-service messages can be pinned. Conversely, the bot must be an administrator with the
     * [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right to pin messages in groups and channels
     * respectively. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  pinned
     * @param messageId Identifier of the message to pin
     * @param disableNotification Pass *True*, if it is not necessary to send a notification to all chat members about
     *  the new pinned message. Notifications are always disabled in channels and private chats.
     * */
    suspend fun pinChatMessage(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageId: Int,
        disableNotification: Boolean? = null,
    ) = request<Boolean> {
        client.post("pinChatMessage") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("disable_notification", disableNotification)
            }
        }
    }

    /**
     * Use this method to remove a message from the list of pinned messages in a chat. In private chats and channel
     * direct messages chats, all messages can be unpinned. Conversely, the bot must be an administrator with the
     * [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right in groups and channels respectively. Returns
     * *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  unpinned
     * @param messageId Identifier of the message to unpin. Required if [businessConnectionId] is specified. If not
     *  specified, the most recent pinned message (by sending date) will be unpinned.
     * */
    suspend fun unpinChatMessage(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageId: Int? = null,
    ) = request<Boolean> {
        client.post("unpinChatMessage") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
        }
    }

    /**
     * Use this method to clear the list of pinned messages in a chat. In private chats and channel direct messages
     * chats, no additional rights are required to unpin all pinned messages. Conversely, the bot must be an
     * administrator with the [canPinMessages][ChatAdministratorRights.canPinMessages] right or the
     * [canEditMessages][ChatAdministratorRights.canEditMessages] right to unpin all pinned messages in groups and
     * channels respectively. Returns *True* on success.
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
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel. Channel
     *  direct messages chats aren't supported; leave the corresponding channel instead.
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
     * conversations, current username of a user, group or channel, etc.). Returns a [ChatFullInfo] object on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel
     * */
    suspend fun getChat(
        chatId: ChatId
    ) = request<ChatFullInfo> {
        client.post("getChat") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get a list of administrators in a chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel
     * @param returnBots Pass *True* to additionally receive all bots that are administrators of the chat. By default,
     *  bots other than the current bot are omitted.
     * */
    suspend fun getChatAdministrators(
        chatId: ChatId,
        returnBots: Boolean? = null,
    ) = request<List<ChatMember>> {
        client.post("getChatAdministrators") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("return_bots", returnBots)
            }
        }
    }

    /**
     * Use this method to get the number of members in a chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel
     * */
    suspend fun getChatMemberCount(
        chatId: ChatId,
    ) = request<Int> {
        client.post("getChatMemberCount") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Use this method to get information about a member of a chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel
     * @param userId Unique identifier of the target user
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
     * Use this method to get the last messages from the personal chat (i.e., the chat currently added to their profile)
     * of a given user. On success, an array of Message objects is returned.
     *
     * @param userId Unique identifier for the target user
     * @param limit The maximum number of messages to return; 1-20
     * */
    suspend fun getUserPersonalChatMessages(
        userId: ChatId.ID,
        limit: Int,
    ) = request<List<Message>> {
        client.post("getUserPersonalChatMessages") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for
     * this to work and must have the appropriate administrator rights. Use the field
     * [canSetStickerSet][ChatFullInfo.canSetStickerSet] optionally returned in [getChat] requests to check if the bot
     * can use this method. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param stickerSetName Name of the sticker set to be set as the group sticker set
     * */
    suspend fun setChatStickerSet(
        chatId: ChatId,
        stickerSetName: String,
    ) = request<Boolean> {
        client.post("setChatStickerSet") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("sticker_set_name", stickerSetName)
            }
        }
    }

    /**
     * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for
     * this to work and must have the appropriate administrator rights. Use the field
     * [canSetStickerSet][ChatFullInfo.canSetStickerSet] optionally returned in [getChat] requests to check if the bot
     * can use this method. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * */
    suspend fun deleteChatStickerSet(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("deleteChatStickerSet") {
            parameter("chat_id", chatId)
        }
    }

    /** Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. */
    suspend fun getForumTopicIconStickers() = request<List<Sticker>> {
        client.post("getForumTopicIconStickers")
    }

    /**
     * Use this method to create a topic in a forum supergroup chat or a private chat with a user. In the case of a
     * supergroup chat the bot must be an administrator in the chat for this to work and must have
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator right. Returns information about the
     * created topic as a [ForumTopic] object.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param name Topic name, 1-128 characters
     * @param iconColor Color of the topic icon.
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use
     *  [getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     * */
    suspend fun createForumTopic(
        chatId: ChatId,
        name: String,
        iconColor: IconColor? = null,
        iconCustomEmojiId: String? = null,
    ) = request<ForumTopic> {
        client.post("createForumTopic") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("icon_color", iconColor)
                appendSerializable("icon_custom_emoji_id", iconCustomEmojiId)
            }
        }
    }

    /**
     * Use this method to edit name and icon of a topic in a forum supergroup chat or a private chat with a user. In the
     * case of a supergroup chat the bot must be an administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights, unless it is the creator of the
     * topic. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageThreadId Unique identifier for the target message thread of the forum topic
     * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be
     *  kept.
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use
     *  [getForumTopicIconStickers] to get all allowed custom emoji identifiers. Pass an empty string to remove the icon
     *  . If not specified, the current icon will be kept.
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

            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("icon_custom_emoji_id", iconCustomEmojiId)
            }
        }
    }

    /**
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat
     * for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
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
     * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat
     * for this to work and must have the [*canManageTopics*][ChatAdministratorRights.canManageTopics] administrator
     * rights, unless it is the creator of the topic. Returns *True* on success.
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
     * Use this method to delete a forum topic along with all its messages in a forum supergroup chat or a private chat
     * with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work and
     * must have the [canDeleteMessages][ChatAdministratorRights.canDeleteMessages] administrator rights. Returns *True*
     * on success.
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
     * Use this method to clear the list of pinned messages in a forum topic in a forum supergroup chat or a private
     * chat with a user. In the case of a supergroup chat the bot must be an administrator in the chat for this to work
     * and must have the [canPinMessages][ChatAdministratorRights.canPinMessages] administrator right in the supergroup.
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
     * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an
     * administrator in the chat for this to work and must have the
     * [canManageTopics][ChatAdministratorRights.canManageTopics] administrator rights. Returns *True* on success.
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

            setFormDataBody {
                appendSerializable("name", name)
            }
        }
    }

    /**
     * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in
     * the chat for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics]
     * administrator rights. Returns *True* on success.
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
     * Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must be an administrator
     * in the chat for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics]
     * administrator rights. The topic will be automatically unhidden if it was hidden. Returns *True* on success.
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
     * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the
     * chat for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics] administrator
     * rights. The topic will be automatically closed if it was open. Returns *True* on success.
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
     * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the
     * chat for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics] administrator
     * rights. The topic will be automatically closed if it was open. Returns *True* on success.
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
     * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator
     * in the chat for this to work and must have the [canManageTopics][ChatAdministratorRights.canManageTopics]
     * administrator rights. The topic will be automatically closed if it was open. Returns *True* on success.
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
     * Use this method to send answers to callback queries sent from
     * [inline keyboards](https://core.telegram.org/bots/features#inline-keyboards). The answer will be displayed
     * to the user as a notification at the top of the chat screen or as an alert. On success, *True* is returned.
     *
     * @param callbackQueryId Unique identifier for the query to be answered
     * @param text Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
     * @param showAlert If *True*, an alert will be shown by the client instead of a notification at the top of
     * the chat screen. Defaults to *false*.
     * @param url URL that will be opened by the user's client. If you have created a [Game] and accepted the
     * conditions via [@BotFather](https://t.me/botfather), specify the URL that opens your game - note that this will
     * only work if the query comes from a [InlineKeyboardButton.callbackGame] button.
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

            setFormDataBody {
                appendSerializable("text", text)
                appendSerializable("show_alert", showAlert)
                appendSerializable("url", url)
                appendSerializable("cache_time", cacheTime)
            }
        }
    }

    /**
     * Use this method to reply to a received guest message.
     *
     * @param guestQueryId Unique identifier for the query to be answered
     * @param result An object describing the message to be sent
     * */
    suspend fun answerGuestQuery(
        guestQueryId: String,
        result: InlineQueryResult,
    ) = request<SentGuestMessage> {
        client.post("answerGuestQuery") {
            parameter("guest_query_id", guestQueryId)

            setFormDataBody {
                appendSerializable("result", result)
            }
        }
    }

    /**
     * Use this method to get the list of boosts added to a chat by a user. Requires administrator rights in the chat.
     *
     * @param chatId Unique identifier for the chat or username of the channel
     * @param userId Unique identifier of the target user
     * */
    suspend fun getUserChatBoosts(
        chatId: ChatId,
        userId: ChatId.ID
    ) = request<UserChatBoosts> {
        client.post("getUserChatBoosts") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to get information about the connection of the bot with a business account.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * */
    suspend fun getBusinessConnection(
        businessConnectionId: String,
    ) = request<BusinessConnection> {
        client.post("getBusinessConnection") {
            parameter("business_connection_id", businessConnectionId)
        }
    }

    /**
     * Use this method to get the token of a managed bot.
     *
     * @param userId User identifier of the managed bot whose token will be returned
     * */
    suspend fun getManagedBotToken(
        userId: Int,
    ) = request<String> {
        client.post("getManagedBotToken") {
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to revoke the current token of a managed bot and generate a new one.
     *
     * @param userId User identifier of the managed bot whose token will be replaced
     * */
    suspend fun replaceManagedBotToken(
        userId: Int,
    ) = request<String> {
        client.post("replaceManagedBotToken") {
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to get the access settings of a managed bot.
     *
     * @param userId User identifier of the managed bot whose access settings will be returned
     * */
    suspend fun getManagedBotAccessSettings(
        userId: Int,
    ) = request<BotAccessSettings> {
        client.post("getManagedBotAccessSettings") {
            parameter("user_id", userId)
        }
    }

    /**
     * Use this method to change the access settings of a managed bot. Returns True on success.
     *
     * @param userId User identifier of the managed bot whose access settings will be changed
     * @param isAccessRestricted Pass *True*, if only selected users can access the bot. The bot's owner can always
     *  access it.
     * @param addedUserIds A list of up to 10 identifiers of users who will have access to the bot in addition to its
     *  owner. Ignored if [isAccessRestricted] is false.
     * */
    suspend fun setManagedBotAccessSettings(
        userId: Int,
        isAccessRestricted: Boolean,
        addedUserIds: List<Int>? = null,
    ) = request<Boolean> {
        client.post("setManagedBotAccessSettings") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("is_access_restricted", isAccessRestricted)
                appendSerializable("added_user_ids", addedUserIds)
            }
        }
    }

    /**
     * Use this method to change the list of the bot's commands. See
     * [this manual](https://core.telegram.org/bots#commands) for more details about bot commands. Returns *True* on
     * success.
     *
     * @param commands A list of bot commands to be set as the list of the bot's commands. At most 100 commands can be
     *  specified.
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to
     *  [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from
     * the given scope, for whose language there are no dedicated commands.
     * */
    suspend fun setMyCommands(
        commands: List<BotCommand>,
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("setMyCommands") {
            setFormDataBody {
                appendSerializable("commands", commands)
                appendSerializable("scope", scope)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion,
     * [higher level commands][BotCommandScope] will be shown to affected users. Returns *True* on success.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to
     *  [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from
     *  the given scope, for whose language there are no dedicated commands.
     * */
    suspend fun deleteMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("deleteMyCommands") {
            setFormDataBody {
                appendSerializable("scope", scope)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user language.
     * If commands aren't set, an empty list is returned.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to
     *  [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from
     *  the given scope, for whose language there are no dedicated commands
     * */
    suspend fun getMyCommands(
        scope: BotCommandScope? = null,
        languageCode: String? = null,
    ) = request<List<BotCommand>> {
        client.post("getMyCommands") {
            setFormDataBody {
                appendSerializable("scope", scope)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to change the bot's name. Returns *True* on success.
     *
     * @param name New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given
     *  language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose
     *  language there is no dedicated name.
     * */
    suspend fun setMyName(
        name: String? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("setMyName") {
            setFormDataBody {
                appendSerializable("name", name)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to get the current bot name for the given user language.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyName(
        languageCode: String? = null,
    ) = request<BotName> {
        client.post("getMyName") {
            setFormDataBody {
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty.
     *
     * @param description New bot description; 0-512 characters. Pass an empty string to remove the dedicated
     *  description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the description will be applied to all users
     *  for whose language there is no dedicated description.
     * */
    suspend fun setMyDescription(
        description: String? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("setMyDescription") {
            setFormDataBody {
                appendSerializable("description", description)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to get the current bot description for the given user language.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyDescription(
        languageCode: String? = null,
    ) = request<BotDescription> {
        client.post("getMyDescription") {
            setFormDataBody {
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent
     * together with the link when users share the bot. Returns *True* on success.
     *
     * @param shortDescription New short description for the bot; 0-120 characters. Pass an empty string to remove the
     *  dedicated short description for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the short description will be applied to all
     *  users for whose language there is no dedicated short description.
     * */
    suspend fun setMyShortDescription(
        shortDescription: String? = null,
        languageCode: String? = null,
    ) = request<Boolean> {
        client.post("setMyShortDescription") {
            setFormDataBody {
                appendSerializable("short_description", shortDescription)
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * UUse this method to get the current bot short description for the given user language.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string
     * */
    suspend fun getMyShortDescription(
        languageCode: String? = null,
    ) = request<BotShortDescription> {
        client.post("getMyShortDescription") {
            setFormDataBody {
                appendSerializable("language_code", languageCode)
            }
        }
    }

    /**
     * Changes the profile photo of the bot. Returns *True* on success.
     *
     * @param photo The new profile photo to set
     * */
    suspend fun setMyProfilePhoto(
        photo: InputProfilePhoto,
    ) = request<Boolean> {
        client.post("setMyProfilePhoto") {
            setFormDataBody {
                appendSerializable("photo", photo)
            }
        }
    }

    /** Removes the profile photo of the bot. Returns *True* on success. */
    suspend fun removeMyProfilePhoto() = request<Boolean> {
        client.post("removeMyProfilePhoto")
    }

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param chatId Unique identifier for the target private chat. If not specified the bot's default menu button will
     *  be changed.
     * @param menuButton [MenuButton] object for the bot's new menu button. Defaults to [MenuButtonDefault].
     * */
    suspend fun setChatMenuButton(
        chatId: ChatId? = null,
        menuButton: MenuButton? = null,
    ) = request<Boolean> {
        client.post("setChatMenuButton") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("menu_button", menuButton)
            }
        }
    }

    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     * Returns *True* on Success.
     *
     * @param chatId Unique identifier for the target private chat. If not specified, the bot's default menu button will
     *  be returned.
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
     * administrator to groups or channels. These rights will be suggested to users, but they are free to modify the
     * list before adding the bot. Returns *True* on success.
     *
     * @param rights An object describing new default administrator rights. If not specified, the default administrator
     *  rights will be cleared.
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels. Otherwise, the
     *  default administrator rights of the bot for groups and supergroups will be changed.
     * */
    suspend fun setMyDefaultAdministratorRights(
        rights: ChatAdministratorRights? = null,
        forChannels: Boolean? = null,
    ) = request<Boolean> {
        client.post("setMyDefaultAdministratorRights") {
            setFormDataBody {
                appendSerializable("rights", rights)
                appendSerializable("for_channels", forChannels)
            }
        }
    }

    /**
     * Use this method to get the current default administrator rights of the bot.
     *
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels. Otherwise, the
     *  default administrator rights of the bot for groups and supergroups will be returned.
     * */
    suspend fun getMyDefaultAdministratorRights(
        forChannels: Boolean? = null,
    ) = request<ChatAdministratorRights> {
        client.post("getMyDefaultAdministratorRights") {
            setFormDataBody {
                appendSerializable("for_channels", forChannels)
            }
        }
    }

    /** Returns the list of gifts that can be sent by the bot to users and channel chats. */
    suspend fun getAvailableGifts() = request<Gifts> {
        client.post("getAvailableGifts")
    }

    /**
     * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver.
     * Returns True on success.
     *
     * @param userId Required if [chatId] is not specified. Unique identifier of the target user who will receive the
     *  gift.
     * @param chatId Required if [userId] is not specified. Unique identifier for the chat or username of the channel
     *  that will receive the gift.
     * @param giftId Identifier of the gift; limited gifts can't be sent to channel chats
     * @param payForUpgrade Pass *True* to pay for the gift upgrade from the bot's balance, thereby making the upgrade
     *  free for the receiver
     * @param text Text that will be shown along with the gift; 0-128 characters
     * @param textParseMode Mode for parsing entities in the text. See [formatting options][ParseMode] for more details.
     * Entities other than [bold][MessageEntityType.BOLD], [italic][MessageEntityType.ITALIC],
     *  [underline][MessageEntityType.UNDERLINE], [strikethrough][MessageEntityType.STRIKETHROUGH],
     *  [spoiler][MessageEntityType.SPOILER], [custom emoji][MessageEntityType.CUSTOM_EMOJI], and
     *  [date time][MessageEntityType.DATE_TIME] are ignored.
     * @param textEntities A list of special entities that appear in the gift text. It can be specified
     *  instead of [textParseMode]. Entities other than [bold][MessageEntityType.BOLD],
     *  [italic][MessageEntityType.ITALIC], [underline][MessageEntityType.UNDERLINE],
     *  [strikethrough][MessageEntityType.STRIKETHROUGH], [spoiler][MessageEntityType.SPOILER],
     *  [custom emoji][MessageEntityType.CUSTOM_EMOJI], and [date time][MessageEntityType.DATE_TIME] are ignored.
     *
     * @see [IMessage.MAX_GIFT_TEXT_LENGTH] // TODO link to limits object
     * */
    suspend fun sendGift(
        userId: ChatId.ID? = null,
        chatId: ChatId? = null,
        giftId: String,
        payForUpgrade: Boolean? = null,
        text: String? = null,
        textParseMode: ParseMode? = null,
        textEntities: List<MessageEntity>? = null,
    ) = request<Boolean> {
        client.post("sendGift") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("gift_id", giftId)
                appendSerializable("pay_for_upgrade", payForUpgrade)
                appendSerializable("text", text)
                appendSerializable("text_parse_mode", textParseMode)
                appendSerializable("text_entities", textEntities)
            }
        }
    }

    /**
     * Gifts a Telegram Premium subscription to the given user. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user who will receive a Telegram Premium subscription
     * @param monthCount Number of months the Telegram Premium subscription will be active for the user; must be one of
     *  3, 6, or 12
     * @param starCount Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3
     *  months, 1500 for 6 months, and 2500 for 12 months
     * @param text Text that will be shown along with the service message about the subscription; 0-128 characters
     * @param textParseMode Mode for parsing entities in the text. See [formatting options][ParseMode] for more details.
     * Entities other than [bold][MessageEntityType.BOLD], [italic][MessageEntityType.ITALIC],
     *  [underline][MessageEntityType.UNDERLINE], [strikethrough][MessageEntityType.STRIKETHROUGH],
     *  [spoiler][MessageEntityType.SPOILER], [custom emoji][MessageEntityType.CUSTOM_EMOJI], and
     *  [date time][MessageEntityType.DATE_TIME] are ignored.
     * @param textEntities A JSON-serialized list of special entities that appear in the gift text. It can be specified
     *  instead of [textParseMode]. Entities other than [bold][MessageEntityType.BOLD],
     *  [italic][MessageEntityType.ITALIC], [underline][MessageEntityType.UNDERLINE],
     *  [strikethrough][MessageEntityType.STRIKETHROUGH], [spoiler][MessageEntityType.SPOILER],
     *  [custom emoji][MessageEntityType.CUSTOM_EMOJI], and [date time][MessageEntityType.DATE_TIME] are ignored.
     * */
    suspend fun giftPremiumSubscription(
        userId: ChatId.ID,
        monthCount: Int,
        starCount: Int,
        text: String? = null,
        textParseMode: String? = null,
        textEntities: List<MessageEntity>? = null,
    ) = request<Boolean> {
        client.post("giftPremiumSubscription") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("month_count", monthCount)
                appendSerializable("star_count", starCount)
                appendSerializable("text", text)
                appendSerializable("text_parse_mode", textParseMode)
                appendSerializable("text_entities", textEntities)
            }
        }
    }

    /**
     * Verifies a user on behalf of the organization which is represented by the bot. Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the
     *  organization isn't allowed to provide a custom verification description.
     * */
    suspend fun verifyUser(
        userId: ChatId.ID,
        customDescription: String? = null,
    ) = request<Boolean> {
        client.post("verifyUser") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("custom_description", customDescription)
            }
        }
    }

    /**
     * Verifies a chat [on behalf of the organization](https://telegram.org/verify#third-party-verification) which is
     * represented by the bot. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param customDescription Custom description for the verification; 0-70 characters. Must be empty if the
     *  organization isn't allowed to provide a custom verification description.
     * */
    suspend fun verifyChat(
        chatId: ChatId,
        customDescription: String? = null,
    ) = request<Boolean> {
        client.post("verifyChat") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("custom_description", customDescription)
            }
        }
    }

    /**
     * Removes verification from a user who is currently verified
     * [on behalf of the organization](https://telegram.org/verify#third-party-verification) represented by the bot.
     * Returns *True* on success.
     *
     * @param userId Unique identifier of the target user
     * */
    suspend fun removeUserVerification(
        userId: ChatId.ID,
    ) = request<Boolean> {
        client.post("removeUserVerification") {
            parameter("user_id", userId)
        }
    }

    /**
     * Removes verification from a chat that is currently verified
     * [on behalf of the organization](https://telegram.org/verify#third-party-verification) represented by the bot.
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * */
    suspend fun removeChatVerification(
        chatId: ChatId,
    ) = request<Boolean> {
        client.post("removeChatVerification") {
            parameter("chat_id", chatId)
        }
    }

    /**
     * Marks incoming message as read on behalf of a business account. Requires the [BusinessBotRights.canReadMessages]
     * right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which to read the message
     * @param chatId Unique identifier of the chat in which the message was received. The chat must have been active in
     *  the last 24 hours.
     * @param messageId Unique identifier of the message to mark as read
     * */
    suspend fun readBusinessMessage(
        businessConnectionId: String,
        chatId: ChatId,
        messageId: Int,
    ) = request<Boolean> {
        client.post("readBusinessMessage") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("message_id", messageId)
            }
        }
    }

    /**
     * Delete messages on behalf of a business account. Requires the [BusinessBotRights.canDeleteSentMessages] right to
     * delete messages sent by the bot itself, or the [BusinessBotRights.canDeleteAllMessages] right to delete any
     * message. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which to delete the
     *  messages
     * @param messageIds A list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See
     * [deleteMessage] for limitations on which messages can be deleted.
     * */
    suspend fun deleteBusinessMessages(
        businessConnectionId: String,
        messageIds: List<Int>,
    ) = request<Boolean> {
        client.post("deleteBusinessMessages") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("message_ids", messageIds)
            }
        }
    }

    /**
     * Changes the first and last name of a managed business account. Requires the [BusinessBotRights.canEditName] right
     * . Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param firstName The new value of the first name for the business account; 1-64 characters
     * @param lastName The new value of the last name for the business account; 0-64 characters
     * */
    suspend fun setBusinessAccountName(
        businessConnectionId: String,
        firstName: String,
        lastName: String? = null,
    ) = request<Boolean> {
        client.post("setBusinessAccountName") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("first_name", firstName)
                appendSerializable("last_name", lastName)
            }
        }
    }

    /**
     * Changes the username of a managed business account. Requires the [BusinessBotRights.canEditUsername] right.
     * Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param username The new value of the username for the business account; 0-32 characters
     * */
    suspend fun setBusinessAccountUsername(
        businessConnectionId: String,
        username: String? = null,
    ) = request<Boolean> {
        client.post("setBusinessAccountUsername") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("username", username)
            }
        }
    }

    /**
     * Changes the bio of a managed business account. Requires the [BusinessBotRights.canEditBio] business right.
     * Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param bio The new value of the bio for the business account; 0-140 characters
     * */
    suspend fun setBusinessAccountBio(
        businessConnectionId: String,
        bio: String? = null,
    ) = request<Boolean> {
        client.post("setBusinessAccountBio") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("bio", bio)
            }
        }
    }

    /**
     * Changes the profile photo of a managed business account. Requires the
     * [BusinessBotRights.canEditProfilePhoto] right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param photo The new profile photo to set
     * @param isPublic Pass *True* to set the public photo, which will be visible even if the main photo is hidden by the
     *  business account's privacy settings. An account can have only one public photo.
     * */
    suspend fun setBusinessAccountProfilePhoto(
        businessConnectionId: String,
        photo: InputProfilePhoto,
        isPublic: Boolean? = null,
    ) = request<Boolean> {
        client.post("setBusinessAccountProfilePhoto") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("photo", photo)
                appendSerializable("is_public", isPublic)
            }
        }
    }

    /**
     * Removes the current profile photo of a managed business account. Requires the
     * [BusinessBotRights.canEditProfilePhoto] right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param isPublic Pass *True* to remove the public photo, which is visible even if the main photo is hidden by the
     *  business account's privacy settings. After the main photo is removed, the previous profile photo (if present)
     *  becomes the main photo.
     * */
    suspend fun removeBusinessAccountProfilePhoto(
        businessConnectionId: String,
        isPublic: Boolean? = null,
    ) = request<Boolean> {
        client.post("removeBusinessAccountProfilePhoto") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("is_public", isPublic)
            }
        }
    }

    /**
     * Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the
     * [BusinessBotRights.canChangeGiftSettings] right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param showGiftButton Pass *True*, if a button for sending a gift to the user or by the business account must
     *  always be shown in the input field
     * @param acceptedGiftTypes Types of gifts accepted by the business account
     * */
    suspend fun setBusinessAccountGiftSettings(
        businessConnectionId: String,
        showGiftButton: Boolean,
        acceptedGiftTypes: AcceptedGiftTypes,
    ) = request<Boolean> {
        client.post("setBusinessAccountGiftSettings") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("show_gift_button", showGiftButton)
                appendSerializable("accepted_gift_types", acceptedGiftTypes)
            }
        }
    }

    /**
     * Returns the amount of Telegram Stars owned by a managed business account. Requires the
     * [BusinessBotRights.canViewGiftsAndStars] right.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * */
    suspend fun getBusinessAccountStarBalance(
        businessConnectionId: String,
    ) = request<StarAmount> {
        client.post("getBusinessAccountStarBalance") {
            parameter("business_connection_id", businessConnectionId)
        }
    }

    /**
     * Transfers Telegram Stars from the business account balance to the bot's balance. Requires the
     * [BusinessBotRights.canTransferStars] business bot right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param starCount Number of Telegram Stars to transfer; 1-10000
     * */
    suspend fun transferBusinessAccountStars(
        businessConnectionId: String,
        starCount: Int,
    ) = request<Boolean> {
        client.post("transferBusinessAccountStars") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("star_count", starCount)
            }
        }
    }

    /**
     * Returns the gifts received and owned by a managed business account. Requires the
     * [BusinessBotRights.canViewGiftsAndStars] business bot right.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param excludeUnsaved Pass *True* to exclude gifts that aren't saved to the account's profile page
     * @param excludeSaved Pass *True* to exclude gifts that are saved to the account's profile page
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and
     *  can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times
     * and can't be upgraded to unique
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be
     *  resold or transferred in Telegram
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before
     *  pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use empty string to get
     *  the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100.
     * */
    suspend fun getBusinessAccountGifts(
        businessConnectionId: String,
        excludeUnsaved: Boolean? = null,
        excludeSaved: Boolean? = null,
        excludeUnlimited: Boolean? = null,
        excludeLimitedUpgradable: Boolean? = null,
        excludeLimitedNonUpgradable: Boolean? = null,
        excludeUnique: Boolean? = null,
        excludeFromBlockchain: Boolean? = null,
        sortByPrice: Boolean? = null,
        offset: String? = null,
        limit: Int? = null,
    ) = request<Gifts> {
        client.post("getBusinessAccountGifts") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("exclude_unsaved", excludeUnsaved)
                appendSerializable("exclude_saved", excludeSaved)
                appendSerializable("exclude_unlimited", excludeUnlimited)
                appendSerializable("exclude_limited_upgradable", excludeLimitedUpgradable)
                appendSerializable("exclude_limited_non_upgradable", excludeLimitedNonUpgradable)
                appendSerializable("exclude_unique", excludeUnique)
                appendSerializable("exclude_from_blockchain", excludeFromBlockchain)
                appendSerializable("sort_by_price", sortByPrice)
                appendSerializable("offset", offset)
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Returns the gifts owned and hosted by a user.
     *
     * @param userId Unique identifier of the user
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and
     *  can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times
     * and can't be upgraded to unique
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be
     *  resold or transferred in Telegram
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before
     *  pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use an empty string to
     *  get the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100.
     * */
    suspend fun getUserGifts(
        userId: ChatId.ID,
        excludeUnlimited: Boolean? = null,
        excludeLimitedUpgradable: Boolean? = null,
        excludeLimitedNonUpgradable: Boolean? = null,
        excludeFromBlockchain: Boolean? = null,
        excludeUnique: Boolean? = null,
        sortByPrice: Boolean? = null,
        offset: String? = null,
        limit: Int? = null,
    ) = request<Gifts> {
        client.post("getUserGifts") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("exclude_unlimited", excludeUnlimited)
                appendSerializable("exclude_limited_upgradable", excludeLimitedUpgradable)
                appendSerializable("exclude_limited_non_upgradable", excludeLimitedNonUpgradable)
                appendSerializable("exclude_from_blockchain", excludeFromBlockchain)
                appendSerializable("exclude_unique", excludeUnique)
                appendSerializable("sort_by_price", sortByPrice)
                appendSerializable("offset", offset)
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Returns the gifts owned by a chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel
     * @param excludeUnsaved Pass *True* to exclude gifts that aren't saved to the chat's profile page. Always *True*,
     *  unless the bot has the [ChatAdministratorRights.canPostMessages] right in the channel.
     * @param excludeSaved Pass *True* to exclude gifts that are saved to the chat's profile page. Always *False*,
     * unless the bot has the [ChatAdministratorRights.canPostMessages] right in the channel.
     * @param excludeUnlimited Pass *True* to exclude gifts that can be purchased an unlimited number of times
     * @param excludeLimitedUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times and
     *  can be upgraded to unique
     * @param excludeLimitedNonUpgradable Pass *True* to exclude gifts that can be purchased a limited number of times
     * and can't be upgraded to unique
     * @param excludeFromBlockchain Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be
     *  resold or transferred in Telegram
     * @param excludeUnique Pass *True* to exclude unique gifts
     * @param sortByPrice Pass *True* to sort results by gift price instead of send date. Sorting is applied before
     *  pagination.
     * @param offset Offset of the first entry to return as received from the previous request; use an empty string to
     *  get the first chunk of results
     * @param limit The maximum number of gifts to be returned; 1-100. Defaults to 100.
     * */
    suspend fun getChatGifts(
        chatId: ChatId,
        excludeUnsaved: Boolean? = null,
        excludeSaved: Boolean? = null,
        excludeUnlimited: Boolean? = null,
        excludeLimitedUpgradable: Boolean? = null,
        excludeLimitedNonUpgradable: Boolean? = null,
        excludeFromBlockchain: Boolean? = null,
        excludeUnique: Boolean? = null,
        sortByPrice: Boolean? = null,
        offset: String? = null,
        limit: Int? = null,
    ) = request<Gifts> {
        client.post("getChatGifts") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("exclude_unsaved", excludeUnsaved)
                appendSerializable("exclude_saved", excludeSaved)
                appendSerializable("exclude_unlimited", excludeUnlimited)
                appendSerializable("exclude_limited_upgradable", excludeLimitedUpgradable)
                appendSerializable("exclude_limited_non_upgradable", excludeLimitedNonUpgradable)
                appendSerializable("exclude_from_blockchain", excludeFromBlockchain)
                appendSerializable("exclude_unique", excludeUnique)
                appendSerializable("sort_by_price", sortByPrice)
                appendSerializable("offset", offset)
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Converts a given regular gift to Telegram Stars. Requires the [BusinessBotRights.canConvertGiftsToStars] business
     * bot right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param ownedGiftId Unique identifier of the regular gift that should be converted to Telegram Stars
     * */
    suspend fun convertGiftToStars(
        businessConnectionId: String,
        ownedGiftId: String,
    ) = request<Boolean> {
        client.post("convertGiftToStars") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("owned_gift_id", ownedGiftId)
            }
        }
    }

    /**
     * Upgrades a given regular gift to a unique gift. Requires the [BusinessBotRights.canTransferAndUpgradeGifts]
     * right. Additionally requires the [BusinessBotRights.canTransferStars] right if the upgrade is paid. Returns
     * *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param ownedGiftId Unique identifier of the regular gift that should be upgraded to a unique one
     * @param keepOriginalDetails Pass *True* to keep the original gift text, sender and receiver in the upgraded gift
     * @param starCount The amount of Telegram Stars that will be paid for the upgrade from the business account
     *  balance. If [GiftInfo.prepaidUpgradeStarCount]` > 0` , then pass 0, otherwise, the
     *  [BusinessBotRights.canTransferStars] right is required and [Gift.upgradeStarCount] must be passed.
     * */
    suspend fun upgradeGift(
        businessConnectionId: String,
        ownedGiftId: String,
        keepOriginalDetails: Boolean? = null,
        starCount: Int? = null,
    ) = request<Boolean> {
        client.post("upgradeGift") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("owned_gift_id", ownedGiftId)
                appendSerializable("keep_original_details", keepOriginalDetails)
                appendSerializable("star_count", starCount)
            }
        }
    }

    /**
     * Transfers an owned unique gift to another user. Requires the [BusinessBotRights.canTransferAndUpgradeGifts] right
     * . Requires [BusinessBotRights.canTransferStars] right if the transfer is paid. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param ownedGiftId Unique identifier of the regular gift that should be transferred
     * @param newOwnerChatId Unique identifier of the chat which will own the gift. The chat must be active in the last
     *  24 hours.
     * @param starCount The amount of Telegram Stars that will be paid for the transfer from the business account
     *  balance. If positive, then the [BusinessBotRights.canTransferStars] right is required.
     * */
    suspend fun transferGift(
        businessConnectionId: String,
        ownedGiftId: String,
        newOwnerChatId: Int,
        starCount: Int? = null,
    ) = request<Boolean> {
        client.post("transferGift") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("owned_gift_id", ownedGiftId)
                appendSerializable("new_owner_chat_id", newOwnerChatId)
                appendSerializable("star_count", starCount)
            }
        }
    }

    /**
     * Posts a story on behalf of a managed business account. Requires the [BusinessBotRights.canManageStories] right.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param content Content of the story
     * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`,
     *  `12 * 3600`, `86400`, or `2 * 86400`
     * @param caption Caption of the story, 0-2048 characters after entities parsing
     * @param parseMode Mode for parsing entities in the story caption. See formatting options for more details.
     * @param captionEntities A list of special entities that appear in the caption, which can be
     *  specified instead of parse_mode
     * @param areas A list of clickable areas to be shown on the story
     * @param postToChatPage Pass True to keep the story accessible after it expires
     * @param protectContent Pass True if the content of the story must be protected from forwarding and screenshotting
     * */
    suspend fun postStory(
        businessConnectionId: String,
        content: InputStoryContent, // TODO test
        activePeriod: Int,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        areas: List<StoryArea>? = null,
        postToChatPage: Boolean? = null,
        protectContent: Boolean? = null,
    ) = request<Story> {
        client.post("postStory") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("content", content)
                appendSerializable("active_period", activePeriod)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("areas", areas)
                appendSerializable("post_to_chat_page", postToChatPage)
                appendSerializable("protect_content", protectContent)
            }
        }
    }

    /**
     * Reposts a story on behalf of a business account from another business account. Both business accounts must be
     * managed by the same bot, and the story on the source account must have been posted (or reposted) by the bot.
     * Requires the [BusinessBotRights.canManageStories] right for both business accounts.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param fromChatId Unique identifier of the chat which posted the story that should be reposted
     * @param fromStoryId Unique identifier of the story that should be reposted
     * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`,
     *  `12 * 3600`, `86400`, or `2 * 86400`
     * @param postToChatPage Pass True to keep the story accessible after it expires
     * @param protectContent Pass True if the content of the story must be protected from forwarding and screenshotting
     * */
    suspend fun repostStory(
        businessConnectionId: String,
        fromChatId: ChatId.ID,
        fromStoryId: Int,
        activePeriod: Int,
        postToChatPage: Boolean? = null,
        protectContent: Boolean? = null,
    ) = request<Story> {
        client.post("repostStory") {
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("from_chat_id", fromChatId)
                appendSerializable("from_story_id", fromStoryId)
                appendSerializable("active_period", activePeriod)
                appendSerializable("post_to_chat_page", postToChatPage)
                appendSerializable("protect_content", protectContent)
            }
        }
    }

    /**
     * Edits a story previously posted by the bot on behalf of a managed business account. Requires the
     * [BusinessBotRights.canManageStories] right.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param storyId Unique identifier of the story to edit
     * @param content Content of the story
     * @param caption Caption of the story, 0-2048 characters after entities parsing
     * @param parseMode Mode for parsing entities in the story caption. See [formatting options][ParseMode] for more
     *  details.
     * @param captionEntities A list of special entities that appear in the caption, which can be
     *  specified instead of parse_mode
     * @param areas A list of clickable areas to be shown on the story
     * */
    suspend fun editStory(
        businessConnectionId: String,
        storyId: Int,
        content: InputStoryContent,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        areas: List<StoryArea>? = null,
    ) = request<Story> {
        client.post("editStory") {
            parameter("business_connection_id", businessConnectionId)
            parameter("story_id", storyId)

            setFormDataBody {
                appendSerializable("content", content)
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("areas", areas)
            }
        }
    }

    /**
     * Deletes a story previously posted by the bot on behalf of a managed business account. Requires the
     * [BusinessBotRights.canManageStories] right. Returns *True* on success.
     *
     * @param businessConnectionId Unique identifier of the business connection
     * @param storyId Unique identifier of the story to delete
     * */
    suspend fun deleteStory(
        businessConnectionId: String,
        storyId: Int,
    ) = request<Boolean> {
        client.post("deleteStory") {
            parameter("business_connection_id", businessConnectionId)
            parameter("story_id", storyId)
        }
    }

    /**
     * Use this method to set the result of an interaction with a [Web App](https://core.telegram.org/bots/webapps)
     * and send a corresponding message on behalf of the user to the chat from which the query originated.
     *
     * @param webAppQueryId Unique identifier for the query to be answered
     * @param result Message to be sent
     * */
    suspend fun answerWebAppQuery(
        webAppQueryId: String,
        result: InlineQueryResult,
    ) = request<SentWebAppMessage> {
        client.post("answerWebAppQuery") {
            parameter("web_app_query_id", webAppQueryId)

            setFormDataBody {
                parameter("result", result)
            }
        }
    }

    /**
     * Stores a message that can be sent by a user of a Mini App.
     *
     * @param userId Unique identifier of the target user that can use the prepared message
     * @param result Message to be sent
     * @param allowUserChats Pass *True* if the message can be sent to private chats with users
     * @param allowBotChats Pass *True* if the message can be sent to private chats with bots
     * @param allowGroupChats Pass *True* if the message can be sent to group and supergroup chats
     * @param allowChannelChats Pass *True* if the message can be sent to channel chats
     * */
    suspend fun savePreparedInlineMessage(
        userId: ChatId.ID,
        result: InlineQueryResult,
        allowUserChats: Boolean? = null,
        allowBotChats: Boolean? = null,
        allowGroupChats: Boolean? = null,
        allowChannelChats: Boolean? = null,
    ) = request<PreparedInlineMessage> {
        client.post("savePreparedInlineMessage") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("result", result)
                appendSerializable("allow_user_chats", allowUserChats)
                appendSerializable("allow_bot_chats", allowBotChats)
                appendSerializable("allow_group_chats", allowGroupChats)
                appendSerializable("allow_channel_chats", allowChannelChats)
            }
        }
    }

    /**
     * Stores a keyboard button that can be used by a user within a Mini App.
     *
     * @param userId Unique identifier of the target user that can use the button
     * @param button An object describing the button to be saved. The button must be of the type
     *  [KeyboardButton.requestUsers], [KeyboardButton.requestChat] , or [KeyboardButton.requestManagedBot].
     * */
    suspend fun savePreparedKeyboardButton(
        userId: ChatId.ID,
        button: KeyboardButton,
    ) = request<PreparedKeyboardButton> {
        client.post("savePreparedKeyboardButton") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("button", button)
            }
        }
    }

    /**
     * Use this method to edit text, rich and game messages. On success, if the edited message is not an inline message,
     * the edited [Message] is returned, otherwise *True* is returned. Note that business messages that were not sent by
     * the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent
     * .
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be
     *  edited was sent
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel.
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param text New text of the message, 1-4096 characters after entity parsing; required if [richMessage] isn't
     *  specified
     * @param parseMode Mode  for parsing entities in the message text
     * @param entities A JSON-serialized list of special entities that appear in message text,
     *  which can be specified instead of parseMode
     * @param linkPreviewOptions Link preview generation options for the message
     * @param richMessage New rich content of the message; required if [text] isn't specified
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageText(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        text: String? = null,
        parseMode: ParseMode? = null,
        entities: List<MessageEntity>? = null,
        linkPreviewOptions: LinkPreviewOptions? = botConfig.linkPreviewOptions,
        richMessage: InputRichMessage? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageText") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("text", text)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("entities", entities)
                appendSerializable("link_preview_options", linkPreviewOptions )
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to edit captions of messages. On success, if the edited message is not an inline message, the
     * edited [Message] is returned, otherwise *True* is returned. Note that business messages that were not sent by the
     * bot and do not contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel.
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param caption New caption of the message, 0-1024 characters after entities parsing
     * @param parseMode Mode  for parsing entities in the message text
     * @param captionEntities List of special entities that appear in the caption,
     *  which can be specified instead of [parseMode]
     * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media.
     *  Supported only for animation, photo and video messages.
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageCaption(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null,
        showCaptionAboveMedia: Boolean? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageCaption") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("caption", caption)
                appendSerializable("parse_mode", parseMode)
                appendSerializable("caption_entities", captionEntities)
                appendSerializable("show_caption_above_media", showCaptionAboveMedia)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to edit animation, audio, document, live photo, photo, or video messages, or to replace to a text
     * or a rich message with a media. If a message is part of a message album, then it can be edited only to an audio
     * for audio albums, only to a document for document albums and to a photo, a live photo or a video otherwise. When
     * an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its
     * [InputFile.FileIdOrUrl]. On success, if the edited message is not an inline message, the edited [Message] is
     * returned, otherwise *True* is returned. Note that business messages that were not sent by the bot and do not
     * contain an inline keyboard can only be edited within **48 hours** from the time they were sent.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be
     *  edited was sent
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel.
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if [chatId] and [messageId] are not specified. Identifier of the inline message.
     * @param media New media content of the message
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageMedia(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        media: InputMedia,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageMedia") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("media", media)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    suspend fun editMessageLiveLocation(
        businessConnectionId: String? = null,
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
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("latitude", latitude)
                appendSerializable("longitude", longitude)
                appendSerializable("horizontal_accuracy", horizontalAccuracy)
                appendSerializable("heading", heading)
                appendSerializable("proximity_alert_radius", proximityAlertRadius)
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    suspend fun stopMessageLiveLocation(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<LiveLocationResponse> {
        client.post("stopMessageLiveLocation") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("reply_markup", keyboardMarkup)
            }
        }
    }

    /**
     * Use this method to edit a checklist on behalf of a connected business account. On success, the edited [Message]
     * is returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param chatId Unique identifier for the target chat or username of the target bot
     * @param messageId Unique identifier for the target message
     * @param checklist An object for the new checklist
     * @param replyMarkup An object for the new inline keyboard for the message
     * */
    suspend fun editMessageChecklist(
        businessConnectionId: String,
        chatId: ChatId,
        messageId: Int,
        checklist: InputChecklist,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("editMessageChecklist") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("checklist", checklist)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline
     * message, the edited [Message] is returned, otherwise *True* is returned. Note that business messages that were
     * not sent by the bot and do not contain an inline keyboard can only be edited within **48 hours** from the time
     * they were sent.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param chatId Required if [inlineMessageId] is not specified. Unique identifier for the target chat or username
     *  of the target bot, supergroup or channel.
     * @param messageId Required if [inlineMessageId] is not specified. Identifier of the message to edit.
     * @param inlineMessageId Required if chatId and messageId are not specified. Identifier of the inline message.
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun editMessageReplyMarkup(
        businessConnectionId: String? = null,
        chatId: ChatId? = null,
        messageId: Int? = null,
        inlineMessageId: String? = null,
        replyMarkup: KeyboardMarkup? = null
    ) = request<Message> { // TODO should be Message | Boolean
        client.post("editMessageReplyMarkup") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("inline_message_id", inlineMessageId)

            setFormDataBody {
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll] is returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to
     *  be edited was sent
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageId Identifier of the original message with the poll
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards)
     * */
    suspend fun stopPoll(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageId: Int,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Poll> {
        client.post("stopPoll") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to approve a suggested post in a direct messages chat. The bot must have the
     * [ChatAdministratorRights.canPostMessages] right in the corresponding channel chat. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target direct messages chat
     * @param messageId Identifier of a suggested post message to approve
     * @param sendDate Point in time when the post is expected to be published; omit if the date has
     *  already been specified when the suggested post was created. If specified, then the date must be not more than
     *  30 days in the future.
     * */
    suspend fun approveSuggestedPost(
        chatId: ChatId,
        messageId: Int,
        sendDate: ZonedDateTime? = null,
    ) = request<Boolean> {
        client.post("approveSuggestedPost") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("send_date", sendDate)
            }
        }
    }

    /**
     * Use this method to decline a suggested post in a direct messages chat. The bot must have the
     * [ChatAdministratorRights.canManageDirectMessages] right in the corresponding channel chat. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target direct messages chat
     * @param messageId Identifier of a suggested post message to decline
     * @param comment Comment for the creator of the suggested post; 0-128 characters
     * */
    suspend fun declineSuggestedPost(
        chatId: ChatId,
        messageId: Int,
        comment: String? = null,
    ) = request<Boolean> {
        client.post("declineSuggestedPost") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)

            setFormDataBody {
                appendSerializable("comment", comment)
            }
        }
    }

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     * - A message can only be deleted if it was sent less than 48 hours ago.
     * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.
     * - Bots can delete incoming messages in private chats.
     * - Bots granted [ChatAdministratorRights.canPostMessages] permissions can delete outgoing messages in channels.
     * - If the bot is an administrator of a group, it can delete any message there.
     * - If the bot has [ChatAdministratorRights.canDeleteMessages] right in a supergroup or a channel, it can delete
     * any message there.
     * - If the bot has [ChatAdministratorRights.canManageDirectMessages] right in a channel, it can delete any message
     * in the corresponding direct messages chat.
     *
     * Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
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

    /**
     * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found,
     * they are skipped. Returns *True* on success.
     *
     * See [deleteMessage] for limitations on which messages can be deleted
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageIds A list of 1-100 identifiers of messages to delete.
     * */
    suspend fun deleteMessages(
        chatId: ChatId,
        messageIds: List<Int>,
    ) = request<Boolean> {
        client.post("deleteMessages") {
            parameter("chat_id", chatId)

            setFormDataBody {
                appendSerializable("message_ids", messageIds)
            }
        }
    }

    /**
     * Use this method to remove a reaction from a message in a group or a supergroup chat. The bot must have the
     * [ChatAdministratorRights.canDeleteMessages] right in the chat. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param messageId Identifier of the target message
     * @param userId Identifier of the user whose reaction will be removed, if the reaction was added by a user
     * @param actorChatId Identifier of the chat whose reaction will be removed, if the reaction was added by a chat
     * */
    suspend fun deleteMessageReaction(
        chatId: ChatId,
        messageId: Int,
        userId: Int? = null,
        actorChatId: Int? = null,
    ) = request<Boolean> {
        client.post("deleteMessageReaction") {
            parameter("chat_id", chatId)
            parameter("message_id", messageId)
            parameter("user_id", userId)
            parameter("actor_chat_id", actorChatId)
        }
    }

    /**
     * Use this method to remove up to 10000 recent reactions in a group or a supergroup chat added by a given user or
     * chat. The bot must have the [ChatAdministratorRights.canDeleteMessages] right in the chat. Returns *True* on
     * success.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup
     * @param userId Identifier of the user whose reactions will be removed, if the reactions were added by a user
     * @param actorChatId Identifier of the chat whose reactions will be removed, if the reactions were added by a chat
     * */
    suspend fun deleteAllMessageReactions(
        chatId: ChatId,
        userId: Int? = null,
        actorChatId: Int? = null,
    ) = request<Boolean> {
        client.post("deleteAllMessageReactions") {
            parameter("chat_id", chatId)
            parameter("user_id", userId)
            parameter("actor_chat_id", actorChatId)
        }
    }

    suspend fun sendSticker(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        sticker: InputFile,
        emoji: String? = null,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        keyboardMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendSticker") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("sticker", sticker)
                appendSerializable("emoji", emoji)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", keyboardMarkup)
            }
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
     *
     * @param customEmojiIds A list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     * */
    suspend fun getCustomEmojiStickers(
        customEmojiIds: List<String>,
    ) = request<List<Sticker>> {
        client.post("getCustomEmojiStickers") {
            setFormDataBody {
                appendSerializable("custom_emoji_ids", customEmojiIds)
            }
        }
    }

    /**
     * Use this method to upload a .PNG file with a sticker for later use in [createNewStickerSet], [addStickerToSet],
     * or [replaceStickerInSet] methods (the file can be used multiple times). Returns the uploaded [File] on success.
     *
     * @param userId User identifier of sticker file owner
     * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See
     * [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements.
     * @param stickerFormat Format of the sticker
     * */
    suspend fun uploadStickerFile(
        userId: ChatId.ID,
        sticker: InputFile,
        stickerFormat: StickerFormat,
    ) = request<File> {
        client.post("uploadStickerFile") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("sticker", sticker)
                appendSerializable("sticker_format", stickerFormat)
            }
        }
    }

    /**
     * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus
     * created. Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals). Can contain only
     *  English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and
     *  must end in `"_by_<bot_username>"`. `<bot_username>` is case insensitive. 1-64 characters. You can use
     *  [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for bot in context.
     * @param title Sticker set title, 1-64 characters
     * @param stickers List of stickers
     * @param stickerType Type of stickers in the set. By default, a regular sticker set is created.
     * @param needsRepainting Pass *True* if stickers in the sticker set must be repainted to the color of text when
     *  used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color
     *  based on context; for custom emoji sticker sets only
     * */
    suspend fun createNewStickerSet(
        userId: ChatId.ID,
        name: String,
        title: String,
        stickers: List<InputSticker>,
        stickerType: StickerType? = null,
        needsRepainting: Boolean? = null
    ) = request<Boolean> {
        client.post("createNewStickerSet") {
            parameter("user_id", userId)
            parameter("name", name)

            setFormDataBody {
                appendSerializable("title", title)
                appendSerializable("stickers", stickers)
                appendSerializable("sticker_type", stickerType)
                appendSerializable("needs_repainting", needsRepainting)
            }
        }
    }

    /**
     * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers.
     * Other sticker sets can have up to 120 stickers. Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals). Can contain only
     *  English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and
     *  must end in `"_by_<bot_username>"`. `<bot_username>` is case insensitive. 1-64 characters. You can use
     *  [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for bot in context.
     * @param sticker Information about the added sticker. If exactly the same sticker had already been added to the set
     * , then the set isn't changed.
     * */
    suspend fun addStickerToSet(
        userId: ChatId.ID,
        name: String,
        sticker: InputSticker,
    ) = request<Boolean> {
        client.post("addStickerToSet") {
            parameter("user_id", userId)
            parameter("name", name)

            setFormDataBody {
                appendSerializable("sticker", sticker)
            }
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

            setFormDataBody {
                appendSerializable("position", position)
            }
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
     * Use this method to replace an existing sticker in a sticker set with a new one.The method is equivalent to
     * calling [deleteStickerFromSet], then [addStickerToSet], then [setStickerPositionInSet]. Returns *True* on success
     * .
     *
     * @param userId User identifier of the sticker set owner
     * @param name Sticker set name
     * @param oldSticker File identifier of the replaced sticker
     * @param sticker An object with information about the added sticker. If exactly the same sticker had already been
     *  added to the set, then the set remains unchanged.
     * */
    suspend fun replaceStickerInSet(
        userId: ChatId.ID,
        name: String,
        oldSticker: String,
        sticker: InputSticker,
    ) = request<Boolean> {
        client.post("replaceStickerInSet") {
            parameter("user_id", userId)
            parameter("name", name)

            setFormDataBody {
                appendSerializable("old_sticker", oldSticker)
                appendSerializable("sticker", sticker)
            }
        }
    }

    /**
     * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must
     * belong to a sticker set created by the bot. Returns *True* on success.
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

            setFormDataBody {
                appendSerializable("emoji_list", emojiList)
            }
        }
    }

    /**
     * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong
     * to a sticker set created by the bot. Returns *True* on success.
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

            setFormDataBody {
                appendSerializable("keywords", keywords)
            }
        }
    }

    /**
     * Use this method to change the [mask position][MaskPosition] of a mask sticker. The sticker must belong to a
     * sticker set that was created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param maskPosition position where the mask should be placed on faces. Omit the parameter to remove the mask
     *  position.
     * */
    suspend fun setStickerMaskPosition(
        sticker: String,
        maskPosition: MaskPosition?,
    ) = request<Boolean> {
        client.post("setStickerMaskPosition") {
            parameter("sticker", sticker)

            setFormDataBody {
                appendSerializable("mask_position", maskPosition)
            }
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

            setFormDataBody {
                appendSerializable("title", title)
            }
        }
    }

    /**
     * Use this method to set the thumbnail of a regular or mask sticker set.
     * The format of the thumbnail file must match the format of the stickers in the set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param userId User identifier of the sticker set owner
     * @param thumbnail A **PNG** image with the thumbnail, must be up to 128 kilobytes in size and have width and
     *  height exactly 100px, or a **TGS** animation with the thumbnail up to 32 kilobytes in size; see
     *  [Animation Requirements](https://core.telegram.org/stickers#animation-requirements) for animated sticker
     *  technical requirements, or a **.WEBM** video with the thumbnail up to 32 kilobytes in size; see
     *  [Video Requirements](https://core.telegram.org/stickers#video-requirements) for video sticker technical
     *  requirements. Pass an [InputFile.FileIdOrUrl] to send a file that already exists on the Telegram servers, pass
     *  an [InputFile.FileIdOrUrl] for Telegram to get a file from the Internet, or upload a new one.
     *  [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files). Animated sticker set
     *  thumbnails can't be uploaded via HTTP URL.
     * @param format Format of the thumbnail
     * */
    suspend fun setStickerSetThumbnail(
        name: String,
        userId: ChatId.ID,
        thumbnail: InputFile? = null,
        format: StickerFormat,
    ) = request<Boolean> {
        client.post("setStickerSetThumbnail") {
            parameter("name", name)
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("format", format)
                appendSerializable("thumbnail", thumbnail)
            }
        }
    }

    /**
     * Use this method to set the thumbnail of a custom emoji sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the
     *  thumbnail and use the first sticker as the thumbnail.
     * */
    suspend fun setCustomEmojiStickerSetThumbnail(
        name: String,
        customEmojiId: String? = null,
    ) = request<Boolean> {
        client.post("setCustomEmojiStickerSetThumbnail") {
            parameter("name", name)

            setFormDataBody {
                appendSerializable("custom_emoji_id", customEmojiId)
            }
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
     * Use this method to send rich messages. If the message contains a block with a media element, then the bot must
     * have the right to send the media to the chat. On success, the sent [Message] is returned.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent. Bot can send rich messages on behalf of a business account only if the corresponding user can send rich
     *  messages.
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required
     *  if the message is sent to a direct messages chat
     * @param richMessage The message to be sent
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send; for direct
     *  messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is
     *  automatically declined.
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup Additional interface options. An object for an inline keyboard, custom reply
     *  keyboard, instructions to remove a reply keyboard or to force a reply from the user.
     * */
    suspend fun sendRichMessage(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        richMessage: InputRichMessage,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendRichMessage") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("rich_message", richMessage)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to stream a partial rich message to a user while the message is being generated. Note that the
     * streamed draft is ephemeral and acts as a temporary 30-second preview - once the output is finalized, you must
     * call [sendRichMessage] with the complete message to persist it in the user's chat. Returns *True* on success.
     *
     * @param chatId Unique identifier for the target private chat
     * @param messageThreadId Unique identifier for the target message thread
     * @param draftId Unique identifier of the message draft; must be non-zero. Changes to drafts with the same
     *  identifier are animated.
     * @param richMessage The partial message to be streamed
     * */
    suspend fun sendRichMessageDraft(
        chatId: Int,
        messageThreadId: Int? = null,
        draftId: Int,
        richMessage: InputRichMessage,
    ) = request<Boolean> {
        client.post("sendRichMessageDraft") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)

            setFormDataBody {
                appendSerializable("draft_id", draftId)
                appendSerializable("rich_message", richMessage)
            }
        }
    }

    /**
     * Use this method to send answers to an inline query. On success, *True* is returned. No more than 50 results per
     * query are allowed.
     *
     * @param inlineQueryId Unique identifier for the answered query
     * @param results List of results for the inline query
     * @param cacheTime The maximum amount of time that the result of the inline query may be cached on the server.
     *  Defaults to 300 seconds.
     * @param isPersonal Pass *True* if results may be cached on the server side only for the user that sent the query.
     *  By default, results may be returned to any user who sends the same query.
     * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive more
     *  results. Pass an empty string if there are no more results or if you don't support pagination. Offset length
     *  can't exceed 64 bytes.
     * @param button Button to be shown above inline query results
     * */
    suspend fun answerInlineQuery(
        inlineQueryId: String,
        results: List<InlineQueryResult>,
        cacheTime: Duration? = null,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        button: InlineQueryResultsButton? = null,
    ) = request<Boolean> {
        client.post("answerInlineQuery") {
            parameter("inline_query_id", inlineQueryId)

            setFormDataBody {
                appendSerializable("results", results)
                appendSerializable("cache_time", cacheTime)
                appendSerializable("is_personal", isPersonal)
                appendSerializable("next_offset", nextOffset)
                appendSerializable("button", button)
            }
        }
    }

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     *
     * @param chatId Unique identifier for the target chat or username of the target bot, supergroup or channel
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your
     *  internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather). Pass an empty string for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param currency Three-letter ISO 4217 currency code, see
     *  [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass [Currency.XTR] for
     *  payments in Telegram Stars.
     * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery
     *  cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in
     *  [Telegram Stars](https://t.me/BotNews/90).
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency (integer,
     *  **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `maxTipAmount = 145`. See the *exp*
     *  parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of
     *  digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported
     *  for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param suggestedTipAmounts An array of suggested amounts of tips in the *smallest units* of the currency (integer
     *  , **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be
     *  positive, passed in a strictly increased order and must not exceed [maxTipAmount].
     * @param startParameter Unique deep-linking parameter. If left empty, *forwarded copies* of the sent message will
     *  have a *Pay* button, allowing multiple users to pay directly from the forwarded message, using the same invoice.
     *  If non-empty, forwarded copies of the sent message will have a URL button with a deep link to the bot (instead
     *  of a *Pay* button), with the value used as the start parameter.
     * @param providerData A data about the invoice, which will be shared with the payment provider. A detailed
     *  description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a
     *  service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order. Ignored for payments in
     *  [Telegram Stars](https://t.me/BotNews/90).
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param needEmail Pass *True*, if you require the user's email address to complete the order. Ignored for payments
     *  in [Telegram Stars](https://t.me/BotNews/90).
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order. Ignored
     *  for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to the provider. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to the provider. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param isFlexible Pass *True*, if the final price depends on the shipping method. Ignored for payments in
     *  [Telegram Stars](https://t.me/BotNews/90).
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param suggestedPostParameters An object containing the parameters of the suggested post to send; for direct
     *  messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is
     *  automatically declined.
     * @param replyParameters Description of the message to reply to message is not found
     * @param replyMarkup An object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     *  If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
     * */
    suspend fun sendInvoice(
        chatId: ChatId,
        messageThreadId: Int? = null,
        directMessagesTopicId: Int? = null,
        title: String,
        description: String,
        payload: String,
        providerToken: String? = null,
        currency: String,
        prices: List<LabeledPrice>,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: List<Int>? = null,
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
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        suggestedPostParameters: SuggestedPostParameters? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendInvoice") {
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)
            parameter("direct_messages_topic_id", directMessagesTopicId)

            setFormDataBody {
                appendSerializable("title", title)
                appendSerializable("description", description)
                appendSerializable("payload", payload)
                appendSerializable("provider_token", providerToken)
                appendSerializable("currency", currency)
                appendSerializable("prices", prices)
                appendSerializable("max_tip_amount", maxTipAmount)
                appendSerializable("suggested_tip_amounts", suggestedTipAmounts)
                appendSerializable("start_parameter", startParameter)
                appendSerializable("provider_data", providerData)
                appendSerializable("photo_url", photoUrl)
                appendSerializable("photo_size", photoSize)
                appendSerializable("photo_width", photoWidth)
                appendSerializable("photo_height", photoHeight)
                appendSerializable("need_name", needName)
                appendSerializable("need_phone_number", needPhoneNumber)
                appendSerializable("need_email", needEmail)
                appendSerializable("need_shipping_address", needShippingAddress)
                appendSerializable("send_phone_number_to_provider", sendPhoneNumberToProvider)
                appendSerializable("send_email_to_provider", sendEmailToProvider)
                appendSerializable("is_flexible", isFlexible)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("suggested_post_parameters", suggestedPostParameters)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    /**
     * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the link will be
     *  created. For payments in [Telegram Stars](https://t.me/BotNews/90) only.
     * @param title Product name, 1-32 characters
     * @param description Product description, 1-255 characters
     * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your
     *  internal processes.
     * @param providerToken Payments provider token, obtained via [@Botfather](tg://BotFather). Pass an empty string for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param currency Three-letter ISO 4217 currency code, see
     *  [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass [Currency.XTR] for
     *  payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment. The
     *  currency must be set to [Currency.XTR] (Telegram Stars) if the parameter is used. Currently, it must always be
     *  2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at the same time,
     *  including multiple concurrent subscriptions from the same user. Subscription price must no exceed 10000 Telegram
     *  Stars.
     * @param prices Price breakdown, a list of components (e.g. product price, tax, discount, delivery cost, delivery
     *  tax, bonus, etc.). Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param maxTipAmount The maximum accepted amount for tips in the *smallest units* of the currency (integer,
     *  **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp*
     *  parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of
     *  digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported
     *  for payments in [Telegram Stars](https://t.me/BotNews/90).
     * @param suggestedTipAmounts An array of suggested amounts of tips in the *smallest units* of the currency (integer
     *  , **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be
     *  positive, passed in a strictly increased order and must not exceed [maxTipAmount].
     * @param providerData A JSON-serialized data about the invoice, which will be shared with the payment provider. A
     *  detailed description of required fields should be provided by the payment provider.
     * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a
     *  service. People like it better when they see what they are paying for.
     * @param photoSize Photo size in bytes
     * @param photoWidth Photo width
     * @param photoHeight Photo height
     * @param needName Pass *True*, if you require the user's full name to complete the order. Ignored for payments in
     *  [Telegram Stars](https://t.me/BotNews/90)
     * @param needPhoneNumber Pass *True*, if you require the user's phone number to complete the order. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90)
     * @param needEmail Pass *True*, if you require the user's email address to complete the order. Ignored for payments
     *  in [Telegram Stars](https://t.me/BotNews/90)
     * @param needShippingAddress Pass *True*, if you require the user's shipping address to complete the order. Ignored
     *  for payments in [Telegram Stars](https://t.me/BotNews/90)
     * @param sendPhoneNumberToProvider Pass *True*, if user's phone number should be sent to the provider. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90)
     * @param sendEmailToProvider Pass *True*, if user's email address should be sent to the provider. Ignored for
     *  payments in [Telegram Stars](https://t.me/BotNews/90)
     * @param isFlexible Pass *True*, if the final price depends on the shipping method. Ignored for payments in
     *  [Telegram Stars](https://t.me/BotNews/90)
     * */
    suspend fun createInvoiceLink(
        businessConnectionId: String? = null,
        title: String,
        description: String,
        payload: String,
        providerToken: String? = null,
        currency: String,
        prices: List<LabeledPrice>,
        subscriptionPeriod: Int? = null,
        maxTipAmount: Int? = null,
        suggestedTipAmounts: List<Int>? = null,
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
            parameter("business_connection_id", businessConnectionId)

            setFormDataBody {
                appendSerializable("title", title)
                appendSerializable("description", description)
                appendSerializable("payload", payload)
                appendSerializable("provider_token", providerToken)
                appendSerializable("currency", currency)
                appendSerializable("prices", prices)
                appendSerializable("subscription_period", subscriptionPeriod)
                appendSerializable("max_tip_amount", maxTipAmount)
                appendSerializable("suggested_tip_amounts", suggestedTipAmounts)
                appendSerializable("provider_data", providerData)
                appendSerializable("photo_url", photoUrl)
                appendSerializable("photo_size", photoSize)
                appendSerializable("photo_width", photoWidth)
                appendSerializable("photo_height", photoHeight)
                appendSerializable("need_name", needName)
                appendSerializable("need_phone_number", needPhoneNumber)
                appendSerializable("need_email", needEmail)
                appendSerializable("need_shipping_address", needShippingAddress)
                appendSerializable("send_phone_number_to_provider", sendPhoneNumberToProvider)
                appendSerializable("send_email_to_provider", sendEmailToProvider)
                appendSerializable("is_flexible", isFlexible)
            }
        }
    }.result

    /**
     * If you sent an invoice requesting a shipping address and the parameter `isFlexible` was specified, the Bot API
     * will send an [Update][Update] with a [shippingQuery][Update.shippingQuery] field to the bot. Use this method to
     * reply to shipping queries. On success, *True* is returned.
     *
     * @param shippingQueryId Unique identifier for the query to be answered
     * @param ok Specify *True* if delivery to the specified address is possible and *False* if there are any problems (
     *  for example, if delivery to the specified address is not possible)
     * @param shippingOptions Required if [ok] is *True*. An array of available shipping options.
     * @param errorMessage Required if [ok] is *False*. Error message in human readable form that explains why it is
     *  impossible to complete the order (e.g. ”Sorry, delivery to your desired address is unavailable”). Telegram will
     *  display this message to the user.
     * */
    suspend fun answerShippingQuery(
        shippingQueryId: String,
        ok: Boolean,
        shippingOptions: List<ShippingOption>? = null,
        errorMessage: String? = null,
    ) = request<Boolean> {
        client.post("answerShippingQuery") {
            parameter("shipping_query_id", shippingQueryId)

            setFormDataBody {
                appendSerializable("ok", ok)
                appendSerializable("shipping_options", shippingOptions)
                appendSerializable("error_message", errorMessage)
            }
        }
    }

    /**
     * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the
     * form of an [Update][Update] with the field [preCheckoutQuery][Update.preCheckoutQuery]. Use this method to
     * respond to such pre-checkout queries. On success, *True* is returned. **Note:** The Bot API must receive an
     * answer within 10 seconds after the pre-checkout query was sent.
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

            setFormDataBody {
                appendSerializable("ok", ok)
                appendSerializable("error_message", errorMessage)
            }
        }
    }

    /**
     * A method to get the current Telegram Stars balance of the bot.
     * */
    suspend fun getMyStarBalance() = request<StarAmount> {
        client.post("getMyStarBalance")
    }

    /**
     * Returns the bot's Telegram Star transactions in chronological order.
     *
     * @param offset Number of transactions to skip in the response
     * @param limit The maximum number of transactions to be retrieved. Values between 1-100 are accepted. Defaults to
     *  100.
     * */
    suspend fun getStarTransactions(
        offset: Int,
        limit: Int,
    ) = request<StarTransactions> {
        client.post("getStarTransactions") {
            setFormDataBody {
                appendSerializable("offset", offset)
                appendSerializable("limit", limit)
            }
        }
    }

    /**
     * Refunds a successful payment in [Telegram Stars](https://t.me/BotNews/90). Returns *True* on success.
     *
     * @param userId Identifier of the user whose payment will be refunded
     * @param telegramPaymentChargeId Telegram payment identifier
     * */
    suspend fun refundStarPayment(
        userId: ChatId.ID,
        telegramPaymentChargeId: String,
    ) = request<Boolean> {
        client.post("refundStarPayment") {
            parameter("user_id", userId)

            setFormDataBody {
                appendSerializable("telegram_payment_charge_id", telegramPaymentChargeId)
            }
        }
    }

    /**
     * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns *True* on
     * success.
     *
     * @param userId Identifier of the user whose subscription will be edited
     * @param telegramPaymentChargeId Telegram payment identifier for the subscription
     * @param isCanceled Pass *True* to cancel extension of the user subscription; the subscription must be active up to
     *  the end of the current subscription period. Pass *False* to allow the user to re-enable a subscription that was
     *  previously canceled by the bot.
     * */
    suspend fun editUserStarSubscription(
        userId: ChatId.ID,
        telegramPaymentChargeId: String,
        isCanceled: Boolean
    ) = request<Boolean> {
        client.post("editUserStarSubscription") {
            parameter("user_id", userId)

            setFormDataBody {
                parameter("telegram_payment_charge_id", telegramPaymentChargeId)
                parameter("is_canceled", isCanceled)
            }
        }
    }

    /**
     * Use this method to send a game.
     *
     * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be
     *  sent
     * @param chatId Unique identifier for the target chat or username of the target bot. Games can't be sent to channel
     *  direct messages chats and channel chats.
     * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups
     *  and private chats of bots with forum topic mode enabled only
     * @param gameShortName Short name of the game, serves as the unique identifier for the game. Set up your games via
     *  [@BotFather](https://t.me/botfather).
     * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages).
     *  Users will receive a notification with no sound.
     * @param protectContent Protects the contents of the sent message from forwarding and saving
     * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring
     *  [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once)
     *  for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
     * @param replyParameters Description of the message to reply to
     * @param replyMarkup An object for an [inline keyboard](http://localhost:63342/bots/features#inline-keyboards). If
     *  empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game.
     * */
    suspend fun sendGame(
        businessConnectionId: String? = null,
        chatId: ChatId,
        messageThreadId: Int? = null,
        gameShortName: String,
        disableNotification: Boolean? = null,
        protectContent: Boolean? = null,
        allowPaidBroadcast: Boolean? = null,
        messageEffectId: String? = null,
        replyParameters: ReplyParameters? = null,
        replyMarkup: KeyboardMarkup? = null,
    ) = request<Message> {
        client.post("sendGame") {
            parameter("business_connection_id", businessConnectionId)
            parameter("chat_id", chatId)
            parameter("message_thread_id", messageThreadId)

            setFormDataBody {
                appendSerializable("game_short_name", gameShortName)
                appendSerializable("disable_notification", disableNotification)
                appendSerializable("protect_content", protectContent)
                appendSerializable("allow_paid_broadcast", allowPaidBroadcast)
                appendSerializable("message_effect_id", messageEffectId)
                appendSerializable("reply_parameters", replyParameters)
                appendSerializable("reply_markup", replyMarkup)
            }
        }
    }

    // TODO [docs]
    suspend fun downloadFile(fileId: String) = downloadFile(getFile(fileId))

    // TODO test
    suspend fun downloadFile(file: File): InputStream? {
        if (file.path == null) return null

        return request<InputStream> {
            val isTestServerPluginInstalled = client.pluginOrNull(TelegramTestServer) != null
            val token = client.plugin(Token).token
            val testPath = if (isTestServerPluginInstalled) "test/" else ""

            client.get("/file/bot$token/$testPath${file.path}") {
                unauthenticated()
            }
        }
    }
}
