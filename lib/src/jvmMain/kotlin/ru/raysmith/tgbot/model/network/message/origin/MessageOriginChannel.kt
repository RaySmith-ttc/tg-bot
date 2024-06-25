package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** The message was originally sent to a channel chat. */
@Serializable
data class MessageOriginChannel(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") override val date: Int,

    /** Channel chat to which the message was originally sent */
    @SerialName("chat") val chat: Chat,

    /** Unique message identifier inside the chat */
    @SerialName("message_id") val messageId: Int,

    /** Signature of the original post author */
    @SerialName("author_signature") val authorSignature: String? = null

) : MessageOrigin() {
    override val type: String = "channel"
}
