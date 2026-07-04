package ru.raysmith.tgbot.model.api.message.origin

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.MessageOriginSerializer
import java.time.ZonedDateTime

/**
 * This object describes the origin of a message. It can be one of
 *
 * - [MessageOriginUser]
 * - [MessageOriginHiddenUser]
 * - [MessageOriginChat]
 * - [MessageOriginChannel]
 * */
@Serializable(MessageOriginSerializer::class)
sealed interface MessageOrigin {

    /** Type of the message origin */
    val type: String

    /** Date the message was sent originally */
    val date: ZonedDateTime
}
