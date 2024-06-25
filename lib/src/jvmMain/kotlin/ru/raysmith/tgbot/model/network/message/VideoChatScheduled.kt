package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a service message about a video chat scheduled in the chat. */
@Serializable
data class VideoChatScheduled(

    /** Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator */
    @SerialName("start_date") val startDate: Int

)

