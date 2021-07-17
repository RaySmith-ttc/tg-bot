package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a service message about a voice chat scheduled in the chat. */
data class VoiceChatScheduled(

    /** Point in time (Unix timestamp) when the voice chat is supposed to be started by a chat administrator */
    @SerialName("start_date") val startDate: Int

)

