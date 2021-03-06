package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a service message about a voice chat ended in the chat. */
data class VoiceChatEnded(

    /** Voice chat duration; in seconds */
    @SerialName("duration") val duration: Int

)