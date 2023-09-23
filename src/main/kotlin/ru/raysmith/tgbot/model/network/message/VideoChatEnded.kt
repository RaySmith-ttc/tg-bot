package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a service message about a video chat ended in the chat. */
@Serializable
data class VideoChatEnded(

    /** Video chat duration in seconds */
    @SerialName("duration") val duration: Int

)