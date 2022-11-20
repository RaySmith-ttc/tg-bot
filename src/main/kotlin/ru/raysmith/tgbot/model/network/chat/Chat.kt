package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ApiCaller
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.toChatId
import ru.raysmith.utils.notNull

@Serializable
/** This object represents a chat. */
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
     * Chat photo. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a> // TODO link all to method
     * */
    @SerialName("photo") val photo: ChatPhoto? = null,

    /**
     * Bio of the other party in a private chat. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("bio") val bio: String? = null,

    /**
     * Description, for groups, supergroups and channel chats. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("description") val description: String? = null,

    /**
     * Primary invite link, for groups, supergroups and channel chats. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("invite_link") val inviteLink: String? = null,

    /**
     * The most recent pinned message (by sending date). Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("pinned_message") val pinnedMessage: Message? = null,

    /**
     * Default chat member permissions, for groups and supergroups. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("permissions") val permissions: ChatPermissions? = null,

    /**
     * For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("slow_mode_delay") val slowModeDelay: Int? = null,

    /**
     * The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Int? = null,

    /**
     * For supergroups, name of group sticker set. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("sticker_set_name") val stickerSetName: String? = null,

    /**
     * True, if the bot can change the group sticker set. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("can_set_sticker_set") val canSetStickerSet: Boolean? = null,

    /**
     * Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa;
     * for supergroups and channel chats. This identifier may be greater than 32 bits and some programming
     * languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits,
     * so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
     * Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("linked_chat_id") val linkedChatId: Int? = null,

    /**
     * For supergroups, the location to which the supergroup is connected. Returned only in getChat.
     * @see <a href='https://core.telegram.org/bots/api#getchat'>getChat</a>
     * */
    @SerialName("location") val location: ChatLocation? = null,
) {

    /**
     * Return full name of the chat
     * @param includeUsername If true and there is a first or last name, a nickname will be added in brackets
     * */
    fun getFullName(includeUsername: Boolean = false): String = buildString {
        append(firstName ?: "")
        if (firstName notNull lastName) {
            append(" ")
        }
        append(lastName ?: "")
        if (includeUsername && username != null && this.isNotEmpty()) {
            append(" (")
            append(username)
            append(")")
        }
    }

    // TODO move ban methods to interface, impl update types. It guarantee a context in a group, supergroup or channel
    /**
     * Ban a user in the group
     *
     * @param context Bot context
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are considered
     * to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     *
     * @throws IllegalArgumentException if the chat is not a group, supergroup or channel
     * */
    fun ban(context: ApiCaller, userId: ChatId.ID, untilDate: Int? = null, revokeMessages: Boolean? = null) {
        require(type == ChatType.GROUP || type == ChatType.SUPERGROUP || type == ChatType.CHANNEL) {
            "Chat must be a group, supergroup or channel"
        }
        val id = when(type) {
            ChatType.GROUP -> id
            else -> username!!.toChatId()
        }
        context.service.banChatMember(id, userId, untilDate, revokeMessages)
    }

    /**
     * Unban a user in the group
     *
     * @param context Bot context
     * @param chatId Unique identifier for the target group or username of the target supergroup or channel
     * (in the format @username)
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     * */
    fun unban(context: ApiCaller, userId: ChatId.ID, onlyIfBanned: Boolean? = null) {
        require(type == ChatType.GROUP || type == ChatType.SUPERGROUP || type == ChatType.CHANNEL) {
            "Chat must be a group, supergroup or channel"
        }
        val id = when(type) {
            ChatType.GROUP -> id
            else -> username!!.toChatId()
        }
        context.service.unbanChatMember(id, userId, onlyIfBanned)
    }
}