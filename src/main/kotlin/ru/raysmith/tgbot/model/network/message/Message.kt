package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.network.*
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.chat.forum.*
import ru.raysmith.tgbot.model.network.giveaway.Giveaway
import ru.raysmith.tgbot.model.network.giveaway.GiveawayCompleted
import ru.raysmith.tgbot.model.network.giveaway.GiveawayCreated
import ru.raysmith.tgbot.model.network.giveaway.GiveawayWinners
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.message.origin.MessageOrigin
import ru.raysmith.tgbot.model.network.payment.SuccessfulPayment
import ru.raysmith.tgbot.model.network.sticker.Sticker
import ru.raysmith.tgbot.model.network.updates.boost.ChatBoostAdded
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.network.TelegramApiException

/** This object represents a message. */
@Serializable
data class Message(

    /** Unique message identifier inside this chat */
    @SerialName("message_id") override val messageId: Int,

    /** Unique identifier of a message thread to which the message belongs; for supergroups only */
    @SerialName("message_thread_id") val messageThreadId: Int? = null,

    /** Sender, empty for messages sent to channels */
    @SerialName("from") val from: User? = null,

    /**
     * Sender of the message, sent on behalf of a chat. The channel itself for channel messages. The supergroup
     * itself for messages from anonymous group administrators. The linked channel for messages automatically
     * forwarded to the discussion group
     * */
    @SerialName("sender_chat") val senderChat: Chat? = null,

    /** If the sender of the message boosted the chat, the number of boosts added by the user */
    @SerialName("sender_boost_count") val senderBoostCount: Int? = null,

    /** Date the message was sent in Unix time */
    @SerialName("sender_business_bot") val senderBusinessBot: User? = null,

    /** Date the message was sent in Unix time */
    @SerialName("date") override val date: Int,

    /**
     * The bot that actually sent the message on behalf of the business account.
     * Available only for outgoing messages sent on behalf of the connected business account.
     * */
    @SerialName("business_connection_id") val businessConnectionId: String? = null,

    /** Conversation the message belongs to */
    @SerialName("chat") override val chat: Chat,

    /** Information about the original message for forwarded messages */
    @SerialName("forward_origin") val forwardOrigin: MessageOrigin? = null,

    /** *True*, if the message is sent to a forum topic */
    @SerialName("is_topic_message") val isTopicMessage: Boolean? = null,

    /** *True*, if the message is a channel post that was automatically forwarded to the connected discussion group */
    @SerialName("is_automatic_forward") val isAutomaticForward: Boolean? = null,

    /**
     * For replies, the original message. Note that the Message object in this field will not contain further
     * [replyToMessage] fields even if it itself is a reply.
     * */
    @SerialName("reply_to_message") val replyToMessage: Message? = null,

    /** Information about the message that is being replied to, which may come from another chat or forum topic */
    @SerialName("external_reply") val externalReply: ExternalReplyInfo? = null,

    /** For replies that quote part of the original message, the quoted part of the message */
    @SerialName("quote") val quote: TextQuote? = null,

    /** For replies to a story, the original story */
    @SerialName("reply_to_story") val replyToStory: Story? = null,

    /** Bot through which the message was sent */
    @SerialName("via_bot") val viaBot: User? = null,

    /** Date the message was last edited in Unix time */
    @SerialName("edit_date") val editDate: Int? = null, // TODO change to ZonedDateTime; create serializer

    /** *True*, if the message can't be forwarded */
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,

    /**
     * True, if the message was sent by an implicit action, for example, as an away or a greeting business message,
     * or as a scheduled message
     * */
    @SerialName("is_from_offline") val isFromOffline: Boolean? = null,

    /** The unique identifier of a media message group this message belongs to */
    @SerialName("media_group_id") val mediaGroupId: String? = null,

    /**
     * Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
     * */
    @SerialName("author_signature") val authorSignature: String? = null,

    /** For text messages, the actual UTF-8 text of the message, 0-4096 characters */
    @SerialName("text") val text: String? = null,

    /** For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text */
    @SerialName("entities") val entities: List<MessageEntity>? = null,

    /**
     * Options used for link preview generation for the message, if it is a text message and link preview options
     * were changed
     * */
    @SerialName("link_preview_options") val linkPreviewOptions: LinkPreviewOptions? = null,

    /**
     * Message is an animation, information about the animation. For backward compatibility, when this field is set,
     * the *document* field will also be set
     * */
    @SerialName("animation") val animation: Animation? = null,

    /** Message is an audio file, information about the file */
    @SerialName("audio") val audio: Audio? = null,

    /** Message is a general file, information about the file */
    @SerialName("document") val document: Document? = null,

    /** Message is a photo, available sizes of the photo */
    @SerialName("photo") val photo: List<PhotoSize>? = null,

    /** Message is a sticker, information about the sticker */
    @SerialName("sticker") val sticker: Sticker? = null,

    /** Message is a forwarded story */
    @SerialName("story") val story: Story? = null,

    /** Message is a video, information about the video */
    @SerialName("video") val video: Video? = null,

    /**
     * Message is a [video note](https://telegram.org/blog/video-messages-and-telescope), information about the
     * video message
     * */
    @SerialName("video_note") val videoNote: VideoNote? = null,

    /** Message is a voice message, information about the file */
    @SerialName("voice") val voice: Voice? = null,

    /** Caption for the animation, audio, document, photo, video or voice, 0-1024 characters */
    @SerialName("caption") val caption: String? = null,

    /**
     * For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
     * */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** *True*, if the message media is covered by a spoiler animation */
    @SerialName("has_media_spoiler") val hasMediaSpoiler: Boolean? = null,

    /** Message is a shared contact, information about the contact */
    @SerialName("contact") val contact: Contact? = null,

    /** Message is a dice with random value */
    @SerialName("dice") val dice: Dice? = null,

    // TODO [game support] add game field (https://core.telegram.org/bots/api#message)

    /** Message is a native poll, information about the poll */
    @SerialName("poll") val poll: Poll? = null,

    /**
     * Message is a venue, information about the venue. For backward compatibility, when this field is set,
     * the location field will also be set
     * */
    @SerialName("venue") val venue: Venue? = null,

    /** Message is a shared location, information about the location */
    @SerialName("location") val location: Location? = null,

    /**
     * New members that were added to the group or supergroup and information about them
     * (the bot itself may be one of these members)
     * */
    @SerialName("new_chat_members") val newChatMembers: List<User>? = null,

    /** A member was removed from the group, information about them (this member may be the bot itself) */
    @SerialName("left_chat_member") val leftChatMember: User? = null,

    /** A chat title was changed to this value */
    @SerialName("new_chat_title") val newChatTitle: String? = null,

    /** A chat photo was change to this value */
    @SerialName("new_chat_photo") val newChatPhoto: List<PhotoSize>? = null,

    /** Service message: the chat photo was deleted */
    @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,

    /** Service message: the group has been created */
    @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,

    /**
     * Service message: the supergroup has been created. This field can't be received in a message coming through
     * updates, because bot can't be a member of a supergroup when it is created. It can only be found in
     * [replyToMessage] if someone replies to a very first message in a directly created supergroup.
     * */
    @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,

    /**
     * Service message: the channel has been created. This field can't be received in a message coming through updates,
     * because bot can't be a member of a channel when it is created. It can only be found in [replyToMessage]
     * if someone replies to a very first message in a channel.
     * */
    @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,

    /** Service message: auto-delete timer settings changed in the chat */
    @SerialName("message_auto_delete_timer_changed") val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,

    /**
     * The group has been migrated to a supergroup with the specified identifier. This number may have more than 32
     * significant bits and some programming languages may have difficulty/silent defects in interpreting it.
     * But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe
     * for storing this identifier.
     * */
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long? = null,

    /**
     * The supergroup has been migrated from a group with the specified identifier. This number may have more than 32
     * significant bits and some programming languages may have difficulty/silent defects in interpreting it.
     * But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe
     * for storing this identifier.
     * */
    @SerialName("migrate_from_chat_id") val migrateFromChatId: Long? = null,

    /**
     * Specified message was pinned. Note that the Message object in this field will not contain further
     * [replyToMessage] fields even if it is itself a reply.
     * */
    @SerialName("pinned_message") val pinnedMessage: MaybeInaccessibleMessage? = null,

    /**
     * Message is an invoice for a payment, information about the invoice.
     * @see <a href="https://core.telegram.org/bots/api#payments">More about payments »</a>
     * */
    @SerialName("invoice") val invoice: Invoice? = null,

    /**
     * Message is a service message about a successful payment, information about the payment.
     * @see <a href="https://core.telegram.org/bots/api#payments">More about payments »</a>
     * */
    @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null,

    /** Service message: users were shared with the bot */
    @SerialName("users_shared") val usersShared: UsersShared? = null,

    /** Service message: a chat was shared with the bot */
    @SerialName("chat_shared") val chatShared: ChatShared? = null,

    /**
     * The domain name of the website on which the user has logged in.
     * @see <a href="https://core.telegram.org/widgets/login">More about Telegram Login »</a>
     * */
    @SerialName("connected_website") val connectedWebsite: String? = null,

    /** Service message: the user allowed the bot added to the attachment menu to write messages */
    /**
     * Service message: the user allowed the bot to write messages after adding it to the attachment or side menu,
     * launching a Web App from a link, or accepting an explicit request from
     * a Web App sent by the method requestWriteAccess
     * */
    @SerialName("write_access_allowed") val writeAccessAllowed: WriteAccessAllowed? = null,

    // TODO [passport support] add passport_data field (https://core.telegram.org/bots/api#message)

    /** Service message. A user in the chat triggered another user's proximity alert while sharing Live Location. */
    @SerialName("proximity_alert_triggered") val proximityAlertTriggered: ProximityAlertTriggered? = null,

    /** Service message: user boosted the chat */
    @SerialName("boost_added") val boostAdded: ChatBoostAdded? = null,

    /** Service message: forum topic created */
    @SerialName("forum_topic_created") val forumTopicCreated: ForumTopicCreated? = null,

    /** Service message: forum topic edited */
    @SerialName("forum_topic_edited") val forumTopicEdited: ForumTopicEdited? = null,

    /** Service message: forum topic closed */
    @SerialName("forum_topic_closed") val forumTopicClosed: ForumTopicClosed? = null,

    /** Service message: forum topic reopened */
    @SerialName("forum_topic_reopened") val forumTopicReopened: ForumTopicReopened? = null,

    /** Service message: the 'General' forum topic hidden */
    @SerialName("general_forum_topic_hidden") val generalForumTopicHidden: GeneralForumTopicHidden? = null,

    /** Service message: the 'General' forum topic hidden */
    @SerialName("general_forum_topic_unhidden") val generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null,

    /** Service message: a scheduled giveaway was created */
    @SerialName("giveaway_created") val giveawayCreated: GiveawayCreated? = null,

    /** The message is a scheduled giveaway message */
    @SerialName("giveaway") val giveaway: Giveaway? = null,

    /** A giveaway with public winners was completed */
    @SerialName("giveaway_winners") val giveawayWinners: GiveawayWinners? = null,

    /** Service message: a giveaway without public winners was completed */
    @SerialName("giveaway_completed") val giveawayCompleted: GiveawayCompleted? = null,

    /** Service message: video chat scheduled */
    @SerialName("video_chat_scheduled") val videoChatScheduled: VideoChatScheduled? = null,

    /** Service message: video chat started */
    @SerialName("video_chat_started") val videoChatStarted: VideoChatStarted? = null,

    /** Service message: video chat ended */
    @SerialName("video_chat_ended") val videoChatEnded: VideoChatEnded? = null,

    /** Service message: new participants invited to a video chat */
    @SerialName("video_chat_participants_invited") val videoChatParticipantsInvited: VideoChatParticipantsInvited? = null,

    /** Service message: new participants invited to a voice chat */
    @SerialName("web_app_data") val webAppData: WebAppData? = null,

    /** Inline keyboard attached to the message. `login_url` buttons are represented as ordinary `url` buttons. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null

) : MaybeInaccessibleMessage(), ChatIdHolder {

    /** [Type][MessageType] of the message. It can be text, command or inline data */
    val type = when {
        !entities.isNullOrEmpty() && entities.first().type == MessageEntityType.BOT_COMMAND -> MessageType.COMMAND
        else -> MessageType.TEXT
    }

    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id

    /** Return true if entities has one or more link */
    fun hasUrl() : Boolean = entities?.any { it.type == MessageEntityType.URL } ?: false

    /** Return all paths of a message that contains the requested type */
    fun getAllEntity(type: MessageEntityType): List<String> {
        return mutableListOf<String>().apply {
            entities?.forEach {
                if (it.type == type) {
                    add(text!!.substring(it.offset, it.offset + it.length))
                }
            }
        }
    }

    /** Return sent media object or null */
    fun getMedia(): Media? = when {
        document != null -> document
        animation != null -> animation
        audio != null -> audio
        !photo.isNullOrEmpty() -> photo.last()
        video != null -> video
        videoNote != null -> videoNote
        voice != null -> voice
        else -> null
    }

    /**
     * Use this method to delete a message, including service messages, with the following limitations:
     * - A message can only be deleted if it was sent less than 48 hours ago.
     * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
     * - Bots can delete outgoing messages in private chats, groups, and supergroups.
     * - Bots can delete incoming messages in private chats.
     * - Bots granted can_post_messages permissions can delete outgoing messages in channels.
     * - If the bot is an administrator of a group, it can delete any message there.
     * - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
     *
     * Returns *True* on Success.
     * */
    context(BotContext<*>)
    suspend fun delete() = deleteMessage(chat.id, messageId)

    /**
     *  A safe version of the [delete] method that swallows a [TelegramApiException].
     *  Return true if message success deleted.
     *
     *  *If you don't need a result use [API.deleteMessages]*
     *  */
    context(BotContext<*>)
    suspend fun safeDelete() = try { delete() } catch (e: TelegramApiException) { false }

}