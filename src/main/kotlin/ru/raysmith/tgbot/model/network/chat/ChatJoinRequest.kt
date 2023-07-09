package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.User

@Serializable
/** Represents a join request sent to a chat. */
data class ChatJoinRequest(

    /** Chat to which the request was sent */
    @SerialName("chat") val chat: Chat,

    /** User that sent the join request */
    @SerialName("from") val from: User,

    /**
     * Identifier of a private chat with the user who sent the join request.
     * The bot can use this identifier for 24 hours to send messages until the join request is processed,
     * assuming no other administrator contacted the user.
     * */
    @SerialName("user_chat_id") val userChatId: ChatId.ID,

    /** Date the request was sent in Unix time */
    @SerialName("date") val date: Int,

    /** Chat invite link that was used by the user to send the join request */
    @SerialName("invite_link") val inviteLink: ChatInviteLink? = null,
) : ChatIdHolder {
    override fun getChatId() = chat.id
    override fun getChatIdOrThrow() = chat.id
}