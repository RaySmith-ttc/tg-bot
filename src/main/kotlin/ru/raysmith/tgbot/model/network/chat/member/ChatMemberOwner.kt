package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
/** Represents a chat member that owns the chat and has all administrator privileges. */
data class ChatMemberOwner(
    /** The member's status in the chat, always “creator” */
    override val status: String,
    override val user: User,

    /** *True*, if the user's presence in the chat is hidden */
    @SerialName("is_anonymous") val isAnonymous: Boolean,

    /** Custom title for this user */
    @SerialName("custom_title") val customTitle: String? = null,
) : ChatMember()