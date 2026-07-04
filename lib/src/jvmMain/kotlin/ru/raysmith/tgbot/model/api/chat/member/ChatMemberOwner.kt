package ru.raysmith.tgbot.model.api.chat.member

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Represents a chat member that owns the chat and has all administrator privileges. */
@Serializable
data class ChatMemberOwner(
    @SerialName("user") override val user: User,

    /** *True*, if the user's presence in the chat is hidden */
    @SerialName("is_anonymous") val isAnonymous: Boolean,

    /** Custom title for this user */
    @SerialName("custom_title") val customTitle: String? = null,
) : ChatMember {

    /** The member's status in the chat, always “creator” */
    @Required
    @EncodeDefault
    @SerialName("status")
    override val status: String = "creator"
}
