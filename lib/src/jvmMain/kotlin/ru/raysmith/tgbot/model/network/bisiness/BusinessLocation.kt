package ru.raysmith.tgbot.model.network.bisiness

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.Location

/** Contains information about the location of a Telegram Business account. */
@Serializable
data class BusinessLocation(

    /** Address of the business */
    @SerialName("address") val address: String,

    /** Location of the business */
    @SerialName("location") val location: Location? = null,
)