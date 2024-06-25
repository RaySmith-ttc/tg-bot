package ru.raysmith.tgbot.model.network.message.origin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The message was originally sent by a unknown user. */
@Serializable
data class MessageOriginHiddenUser(

    /** Date the message was sent originally in Unix time */
    @SerialName("date") override val date: Int,

    /** Name of the user that sent the message originally */
    @SerialName("sender_user_name") val senderUserName: String

) : MessageOrigin() {
    override val type: String = "hidden_user"
}