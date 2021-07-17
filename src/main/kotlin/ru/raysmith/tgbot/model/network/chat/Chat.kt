package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.utils.notNull

@Serializable
/** This object represents a chat. */
data class Chat(

    /** Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier. */
    @SerialName("id") val id: Long,

    /** Type of chat, can be either “private”, “group”, “supergroup” or “channel” */
    @SerialName("type") val type: String,

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
     * Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier. Returned only in getChat.
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
     * Возвращает полное имя чата
     *
     * @param includeUsername Если true и есть имя или фамилия, будет добавлен никнейм в скобках
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
}