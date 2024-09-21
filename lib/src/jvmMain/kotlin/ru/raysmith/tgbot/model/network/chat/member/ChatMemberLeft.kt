package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that isn't currently a member of the chat, but may join it themselves. */
@Serializable
data class ChatMemberLeft(
    @SerialName("user") override val user: User,
) : ChatMember() {

    /** The member's status in the chat, always “left” */
    override val status: String = "left"

}