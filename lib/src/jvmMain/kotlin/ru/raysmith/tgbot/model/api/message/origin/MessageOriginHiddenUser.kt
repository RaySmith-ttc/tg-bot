package ru.raysmith.tgbot.model.api.message.origin

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** The message was originally sent by an unknown user. */
@Serializable
data class MessageOriginHiddenUser(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") @Serializable(ZonedDateTimeAsUnixSerializer::class) override val date: ZonedDateTime,

    /** Name of the user that sent the message originally */
    @SerialName("sender_user_name") val senderUserName: String,
) : MessageOrigin {

    /** Type of the message origin, always “hidden_user” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "hidden_user"
}
