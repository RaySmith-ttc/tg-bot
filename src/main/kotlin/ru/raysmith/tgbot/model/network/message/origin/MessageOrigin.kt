package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.MessageOriginSerializer

/**
 * This object describes the origin of a message. It can be one of
 *
 * - [MessageOriginUser]
 * - [MessageOriginHiddenUser]
 * - [MessageOriginChat]
 * - [MessageOriginChannel]
 * */
@Polymorphic
@Serializable(with = MessageOriginSerializer::class)
sealed class MessageOrigin {

    /** Type of the message origin */
    abstract val type: String

    /** Date the message was sent originally in Unix time */
    abstract val date: Int
}
