package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Describes a service message about an ownership change in the chat. */
@Serializable
data class ChatOwnerChanged(

    /** The new owner of the chat */
    @SerialName("new_owner") val newOwner: User,
)
