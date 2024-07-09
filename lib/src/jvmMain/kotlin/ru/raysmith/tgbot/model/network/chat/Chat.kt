package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId

/** This object represents a chat. */
@Serializable
open class Chat(

    /** Unique identifier for this chat. */
    @SerialName("id") override val id: ChatId.ID,

    /** Type of chat, can be either [ChatType.PRIVATE], [ChatType.GROUP], [ChatType.SUPERGROUP] or [ChatType.CHANNEL] */
    @SerialName("type") override val type: ChatType,

    /** Title, for supergroups, channels and group chats */
    @SerialName("title") override val title: String? = null,

    /** Username, for private chats, supergroups and channels if available */
    @SerialName("username") override val username: String? = null,

    /** First name of the other party in a private chat */
    @SerialName("first_name") override val firstName: String? = null,

    /** Last name of the other party in a private chat */
    @SerialName("last_name") override val lastName: String? = null,

    /**
     * *True*, if the supergroup chat is a forum
     * (has [topics](https://telegram.org/blog/topics-in-groups-collectible-usernames#topics-in-groups) enabled)
     * */
    @SerialName("is_forum") override val isForum: Boolean? = null,
) : IChat

