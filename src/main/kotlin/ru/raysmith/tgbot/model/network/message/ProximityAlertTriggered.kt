package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

@Serializable
data class ProximityAlertTriggered(

    /** User that triggered the alert */
    @SerialName("traveler") val traveler: User,

    /** User that set the alert */
    @SerialName("watcher") val watcher: User,

    /** The distance between the users */
    @SerialName("distance") val distance: Int,

    )
