package ru.raysmith.tgbot.model.api.message.origin

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.Chat
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** The message was originally sent on behalf of a chat to a group chat. */
@Serializable
data class MessageOriginChat(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") @Serializable(ZonedDateTimeAsUnixSerializer::class) override val date: ZonedDateTime,

    /** Chat that sent the message originally */
    @SerialName("sender_chat") val senderChat: Chat,

    /** For messages originally sent by an anonymous chat administrator, original message author signature */
    @SerialName("author_signature") val authorSignature: String? = null,
) : MessageOrigin {

    /** Type of the message origin, always “chat” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "chat"
}
