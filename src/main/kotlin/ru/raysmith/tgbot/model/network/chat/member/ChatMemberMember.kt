package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that has no additional privileges or restrictions. */
@Serializable
data class ChatMemberMember(
    /** The member's status in the chat, always “member” */
    override val status: String,
    override val user: User,
) : ChatMember()