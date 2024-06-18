package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that has no additional privileges or restrictions. */
@Serializable
data class ChatMemberMember(
    /** The member's status in the chat, always “member” */
    @SerialName("status") override val status: String,
    @SerialName("user") override val user: User,
) : ChatMember()