package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that isn't currently a member of the chat, but may join it themselves. */
@Serializable
data class ChatMemberLeft(
    /** The member's status in the chat, always “left” */
    override val status: String,
    override val user: User,
) : ChatMember()