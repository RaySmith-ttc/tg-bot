package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** Describes a service message about the chat owner leaving the chat. */
@Serializable
data class ChatOwnerLeft(

    /** The user who will become the new owner of the chat if the previous owner does not return to the chat */
    @SerialName("new_owner") val newOwner: User? = null,
)
