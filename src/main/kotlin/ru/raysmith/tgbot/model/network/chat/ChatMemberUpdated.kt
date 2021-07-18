package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
/** This object represents changes in the status of a chat member. */
data class ChatMemberUpdated(

    /** Chat the user belongs to */
    @SerialName("chat") val chat: Chat,

    /** Performer of the action, which resulted in the change */
    @SerialName("from") val from: User,

    /** Date the change was done in Unix time */
    @SerialName("date") val date: Int,

    /** Previous information about the chat member */
    @SerialName("old_chat_member") val oldChatMember: ChatMember,

    /** New information about the chat member */
    @SerialName("new_chat_member") val newChatMember: ChatMember,

    /** Chat invite link, which was used by the user to join the chat; for joining by invite link events only. */
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null,
)