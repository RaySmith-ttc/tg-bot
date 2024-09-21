package ru.raysmith.tgbot.model.network.chat.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** Represents a chat member that has no additional privileges or restrictions. */
@Serializable
data class ChatMemberMember(
    @SerialName("user") override val user: User,

    /** Date when the user's subscription will expire; Unix time */
    @SerialName("until_date") val untilDate: Int? = null,
) : ChatMember() {

    /** The member's status in the chat, always “member” */
    override val status: String = "member"
}