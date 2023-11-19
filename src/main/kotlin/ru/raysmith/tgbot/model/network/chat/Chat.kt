package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.toChatId

/** This object represents a chat. */
@Serializable
data class Chat(

    /**
     * Unique identifier for this chat. This number may have more than 32 significant bits and some programming
     * languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits,
     * so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     * */
    @SerialName("id") val id: ChatId.ID,

    /** Type of chat, can be either “private”, “group”, “supergroup” or “channel” */
    @SerialName("type") val type: ChatType,

    /** Title, for supergroups, channels and group chats */
    @SerialName("title") val title: String? = null,

    /** Username, for private chats, supergroups and channels if available */
    @SerialName("username") val username: String? = null,

    /** First name of the other party in a private chat */
    @SerialName("first_name") val firstName: String? = null,

    /** Last name of the other party in a private chat */
    @SerialName("last_name") val lastName: String? = null,

    /**
     * *True*, if the supergroup chat is a forum
     * (has [topics](https://telegram.org/blog/topics-in-groups-collectible-usernames#topics-in-groups) enabled)
     * */
    @SerialName("is_forum") val isForum: Boolean? = null,

    /** Chat photo. Returned only in [getChat][API.getChat]. */
    @SerialName("photo") val photo: ChatPhoto? = null,

    /**
     * If non-empty, the list of all
     * [active chat usernames](https://telegram.org/blog/topics-in-groups-collectible-usernames#collectible-usernames);
     * for private chats, supergroups and channels. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("active_usernames") val activeUsernames: List<String>? = null,

    /**
     * Custom emoji identifier of emoji status of the other party in a private chat.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("emoji_status_custom_emoji_id") val emojiStatusCustomEmojiId: String? = null,

    /**
     * Expiration date of the emoji status of the other party in a private chat in Unix time, if any.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("emoji_status_expiration_date") val emojiStatusExpirationDate: Int? = null,

    /**
     * Bio of the other party in a private chat. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("bio") val bio: String? = null,

    /**
     * *True*, if privacy settings of the other party in the private chat allows to use `tg://user?id=<user_id>`
     * links only in chats with the user. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_private_forwards") val hasPrivateForwards: Boolean? = null,

    /**
     * *True*, if the privacy settings of the other party restrict sending voice and video note messages in the
     * private chat. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_restricted_voice_and_video_messages") val hasRestrictedVoiceAndVideoMessages: Boolean? = null,

    /**
     * *True*, if users need to join the supergroup before they can send messages.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("join_to_send_messages") val joinToSendMessages: Boolean? = null,

    /**
     * *True*, if all users directly joining the supergroup need to be approved by supergroup administrators.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("join_by_request") val joinByRequest: Boolean? = null,

    /**
     * Description, for groups, supergroups and channel chats. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("description") val description: String? = null,

    /**
     * Primary invite link, for groups, supergroups and channel chats. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("invite_link") val inviteLink: String? = null,

    /**
     * The most recent pinned message (by sending date). Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("pinned_message") val pinnedMessage: Message? = null,

    /**
     * Default chat member permissions, for groups and supergroups. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("permissions") val permissions: ChatPermissions? = null,

    /**
     * For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("slow_mode_delay") val slowModeDelay: Int? = null,

    /**
     * The time after which all messages sent to the chat will be automatically deleted; in seconds.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Int? = null,

    /**
     * True, if aggressive anti-spam checks are enabled in the supergroup.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_aggressive_anti_spam_enabled") val hasAggressiveAntiSpamEnabled: Boolean? = null,

    /**
     * True, if non-administrators can only get the list of bots and administrators in the chat.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_hidden_members") val hasHiddenMembers: Boolean? = null,

    /**
     * True, if messages from the chat can't be forwarded to other chats.
     * The field is only available to chat administrators. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("has_protected_content") val hasProtectedContent: Boolean? = null,

    /**
     * For supergroups, name of group sticker set. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("sticker_set_name") val stickerSetName: String? = null,

    /**
     * True, if the bot can change the group sticker set. Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null,

    /**
     * Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa;
     * for supergroups and channel chats. This identifier may be greater than 32 bits and some programming
     * languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits,
     * so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("linked_chat_id") val linkedChatId: Int? = null,

    /**
     * For supergroups, the location to which the supergroup is connected.
     * Returned only in [getChat][BotContext.getChat].
     * */
    @SerialName("location") val location: ChatLocation? = null,
) {
    // TODO move ban methods to interface, impl update types. It guarantee a context in a group, supergroup or channel
    /**
     * Ban a user in the group, supergroup or channel
     *
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are considered
     * to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     *
     * @see API.banChatMember
     * @throws IllegalArgumentException if the chat is not a group, supergroup or channel
     * */
    context(BotContext<*>)
    suspend fun ban(userId: ChatId.ID, untilDate: Until? = null, revokeMessages: Boolean? = null) {
        val id = when(type) {
            ChatType.GROUP -> id
            ChatType.SUPERGROUP, ChatType.CHANNEL -> username!!.toChatId()
            else -> error("Chat must be a group, supergroup or channel")
        }
        banChatMember(id, userId, untilDate, revokeMessages)
    }

    /**
     * Unban a user in the group, supergroup or channel
     *
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     *
     * @see API.unbanChatMember
     * @throws IllegalArgumentException if the chat is not a group, supergroup or channel
     * */
    context(BotContext<*>)
    suspend fun unban(userId: ChatId.ID, onlyIfBanned: Boolean? = null) {
        val id = when(type) {
            ChatType.GROUP -> id
            ChatType.SUPERGROUP, ChatType.CHANNEL -> username!!.toChatId()
            else -> error("Chat must be a group, supergroup or channel")
        }
        unbanChatMember(id, userId, onlyIfBanned)
    }
}