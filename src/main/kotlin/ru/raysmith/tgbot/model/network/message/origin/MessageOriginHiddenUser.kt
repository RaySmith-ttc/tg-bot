package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.Serializable

/** The message was originally sent by a unknown user. */
@Serializable
data class MessageOriginHiddenUser(

    /** Date the message was sent originally in Unix time */
    override val date: Int,

    /** Name of the user that sent the message originally */
    val senderUserName: String

) : MessageOrigin() {
    override val type: String = "hidden_user"
}