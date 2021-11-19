package ru.raysmith.tgbot.model.network.message

import ru.raysmith.tgbot.model.network.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.chat.Chat
import ru.raysmith.tgbot.model.network.media.*
import ru.raysmith.tgbot.model.network.payment.SuccessfulPayment
import ru.raysmith.tgbot.network.TelegramApi

@Serializable
/** This object represents a message. */
data class Message(

    /** Unique message identifier inside this chat */
    @SerialName("message_id") val messageId: Long,

    /** Sender, empty for messages sent to channels */
    @SerialName("from") val from: User? = null,

    /**
     * Sender of the message, sent on behalf of a chat. The channel itself for channel messages. The supergroup
     * itself for messages from anonymous group administrators. The linked channel for messages automatically
     * forwarded to the discussion group
     * */
    @SerialName("sender_chat") val senderChat: Chat? = null,

    /** Date the message was sent in Unix time */
    @SerialName("date") val date: Int,

    /** Conversation the message belongs to */
    @SerialName("chat") val chat: Chat,

    /** For forwarded messages, sender of the original message */
    @SerialName("forward_from") val forwardFrom: User? = null,

    /** For messages forwarded from channels or from anonymous administrators, information about the original sender chat */
    @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,

    /** For messages forwarded from channels, identifier of the original message in the channel */
    @SerialName("forward_from_message_id") val forwardFromMessageId: Int? = null,

    /** For messages forwarded from channels, signature of the post author if present */
    @SerialName("forward_signature") val forwardSignature: String? = null,

    /** Sender's name for messages forwarded from users who disallow adding a link to their account in forwarded messages */
    @SerialName("forward_sender_name") val forwardSenderName: String? = null,

    /** For forwarded messages, date the original message was sent in Unix time */
    @SerialName("forward_date") val forwardDate: Int? = null,

    /**
     * For replies, the original message. Note that the Message object in this field will not contain further
     * *reply_to_message* fields even if it itself is a reply.
     * */
    @SerialName("reply_to_message") val replyToMessage: Message? = null,

    /** Bot through which the message was sent */
    @SerialName("via_bot") val viaBot: User? = null,

    /** Date the message was last edited in Unix time */
    @SerialName("edit_date") val editDate: Int? = null,

    /** The unique identifier of a media message group this message belongs to */
    @SerialName("media_group_id") val mediaGroupId: String? = null,

    /** Signature of the post author for messages in channels, or the custom title of an anonymous group administrator */
    @SerialName("author_signature") val authorSignature: String? = null,

    /** For text messages, the actual UTF-8 text of the message, 0-4096 characters */
    @SerialName("text") val text: String? = null,

    /** For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text */
    @SerialName("entities") val entities: List<MessageEntity>? = null,

    /** Message is an animation, information about the animation. For backward compatibility, when this field is set, the *document* field will also be set */
    @SerialName("animation") val animation: Animation? = null,

    /** Message is an audio file, information about the file */
    @SerialName("audio") val audio: Audio? = null,

    /** Message is a general file, information about the file */
    @SerialName("document") val document: Document? = null,

    /** Message is a photo, available sizes of the photo */
    @SerialName("photo") val photo: List<PhotoSize>? = null,

    /** Message is a sticker, information about the sticker */
    @SerialName("sticker") val sticker: Sticker? = null,

    /** Message is a video, information about the video */
    @SerialName("video") val video: Video? = null,

    /**
     * Message is a video note, information about the video message
     *
     * @see <a href="https://telegram.org/blog/video-messages-and-telescope">video note</a>
     * */
    @SerialName("video_note") val videoNote: VideoNote? = null,

    /** Message is a voice message, information about the file */
    @SerialName("voice") val voice: Voice? = null,

    /** Caption for the animation, audio, document, photo, video or voice, 0-1024 characters */
    @SerialName("caption") val caption: String? = null,

    /** For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption */
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,

    /** Message is a shared contact, information about the contact */
    @SerialName("contact") val contact: Contact? = null,

    /** Message is a dice with random value */
    @SerialName("dice") val dice: Dice? = null,

    // TODO [game support] add game field (https://core.telegram.org/bots/api#message)

    // TODO [poll support]
//    /** Message is a native poll, information about the poll */
//    @SerialName("poll") val poll: Poll? = null,
//
    // TODO [venue support]
//    /** Message is a venue, information about the venue. For backward compatibility, when this field is set, the location field will also be set */
//    @SerialName("venue") val venue: Venue? = null,

    /** Message is a venue, information about the venue. For backward compatibility, when this field is set, the location field will also be set */
    @SerialName("location") val location: Location? = null,

    /** New members that were added to the group or supergroup and information about them (the bot itself may be one of these members) */
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
     * reply_to_message if someone replies to a very first message in a directly created supergroup.
     * */
    @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,

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
     * *reply_to_message* fields even if it is itself a reply.
     * */
    @SerialName("pinned_message") val pinnedMessage: Message? = null,

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

    /**
     * The domain name of the website on which the user has logged in.
     * @see <a href="https://core.telegram.org/widgets/login">More about Telegram Login »</a>
     * */
    @SerialName("connected_website") val connectedWebsite: String? = null,
    // TODO [passport support] add passport_data field (https://core.telegram.org/bots/api#message)

    /** Service message. A user in the chat triggered another user's proximity alert while sharing Live Location. */
    @SerialName("proximity_alert_triggered") val proximityAlertTriggered: ProximityAlertTriggered? = null,

    /** Service message: voice chat scheduled */
    @SerialName("voice_chat_scheduled") val voiceChatScheduled: VoiceChatScheduled? = null,

    /** Service message: voice chat started */
    @SerialName("voice_chat_started") val voiceChatStarted: VoiceChatStarted? = null,

    /** Service message: voice chat ended */
    @SerialName("voice_chat_ended") val voiceChatEnded: VoiceChatEnded? = null,

    /** Service message: new participants invited to a voice chat */
    @SerialName("voice_chat_participants_invited") val voiceChatParticipantsInvited: VoiceChatParticipantsInvited? = null,

    /** Inline keyboard attached to the message. `login_url` buttons are represented as ordinary `url` buttons. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null

) {

    /** [Type][MessageType] of the message. It can be text, command or inline data */
    val type = when {
        replyMarkup != null -> MessageType.INLINE_DATA
        entities?.size == 1 && entities.first().type == MessageEntityType.BOT_COMMAND -> MessageType.COMMAND
        else -> MessageType.TEXT
    }

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
        photo != null && photo.isNotEmpty() -> photo.last()
        video != null -> video
        videoNote != null -> videoNote
        voice != null -> voice
        else -> null
    }

    /** Delete a message, including service messages */
    fun delete() = TelegramApi.service.deleteMessage(chat.id.toString(), messageId).execute()
}