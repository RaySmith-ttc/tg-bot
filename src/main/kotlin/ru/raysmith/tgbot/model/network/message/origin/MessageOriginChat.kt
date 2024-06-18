package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** The message was originally sent on behalf of a chat to a group chat. */
@Serializable
data class MessageOriginChat(

    /** Date the message was sent originally in Unix time */
    override val date: Int,

    /** Chat that sent the message originally */
    val senderChat: Chat,

    /** For messages originally sent by an anonymous chat administrator, original message author signature */
    val author_signature: String? = null

) : MessageOrigin() {
    override val type: String = "chat"
}