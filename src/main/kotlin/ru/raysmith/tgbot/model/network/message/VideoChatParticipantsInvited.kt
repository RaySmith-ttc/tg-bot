package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
/** This object represents a service message about new members invited to a video chat. */
data class VideoChatParticipantsInvited(

    /** New members that were invited to the video chat */
    @SerialName("users") val users: List<User>

)