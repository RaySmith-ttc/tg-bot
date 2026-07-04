package ru.raysmith.tgbot.model.api.message.origin

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.serializer.ZonedDateTimeAsUnixSerializer
import java.time.ZonedDateTime

/** The message was originally sent by a known user. */
@Serializable
data class MessageOriginUser(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") @Serializable(ZonedDateTimeAsUnixSerializer::class) override val date: ZonedDateTime,

    /** User that sent the message originally */
    @SerialName("sender_user") val senderUser: User,
) : MessageOrigin {

    /** Type of the message origin, always “user” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "user"
}
