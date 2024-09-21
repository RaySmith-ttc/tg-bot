package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** The message was originally sent on behalf of a chat to a group chat. */
@Serializable
data class MessageOriginChat(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") override val date: Int,

    /** Chat that sent the message originally */
    @SerialName("sender_chat") val senderChat: Chat,

    /** For messages originally sent by an anonymous chat administrator, original message author signature */
    @SerialName("author_signature") val authorSignature: String? = null

) : MessageOrigin() {
    override val type: String = "chat"
}