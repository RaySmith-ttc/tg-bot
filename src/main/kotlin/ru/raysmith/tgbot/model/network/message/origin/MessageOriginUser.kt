package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/** The message was originally sent by a known user. */
@Serializable
data class MessageOriginUser(

    /** Date the message was sent originally in Unix time */
    override val date: Int,

    /** User that sent the message originally */
    val senderUser: User

) : MessageOrigin() {
    override val type: String = "user"
}