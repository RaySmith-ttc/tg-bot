package ru.raysmith.tgbot.model.api.message.origin

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** The message was originally sent to a channel chat. */
@Serializable
data class MessageOriginChannel(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") @Serializable(ZonedDateTimeAsUnixSerializer::class) override val date: ZonedDateTime,

    /** Channel chat to which the message was originally sent */
    @SerialName("chat") val chat: Chat,

    /** Unique message identifier inside the chat */
    @SerialName("message_id") val messageId: Int,

    /** Signature of the original post author */
    @SerialName("author_signature") val authorSignature: String? = null,
) : MessageOrigin {

    /** Type of the message origin, always “channel” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "channel"
}
