package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
/** This object represents a service message about new members invited to a voice chat. */
data class VoiceChatParticipantsInvited(

    /** New members that were invited to the voice chat */
    @SerialName("users") val users: List<User>

)