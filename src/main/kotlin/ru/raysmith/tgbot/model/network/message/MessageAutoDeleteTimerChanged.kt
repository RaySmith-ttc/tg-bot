package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a service message about a change in auto-delete timer settings. */
data class MessageAutoDeleteTimerChanged(

    /** New auto-delete time for messages in the chat */
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Int
)
