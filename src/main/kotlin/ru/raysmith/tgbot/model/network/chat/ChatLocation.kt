package ru.raysmith.tgbot.model.network.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.Location

/** Represents a location to which a chat is connected. */
@Serializable
data class ChatLocation(

    /** The location to which the supergroup is connected. Can't be a live location. */
    @SerialName("location") val location: Location,

    /** Location address; 1-64 characters, as defined by the chat owner */
    @SerialName("address") val address: String,
)
