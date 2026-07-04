package ru.raysmith.tgbot.model.api.business

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.Location

/** Contains information about the location of a Telegram Business account. */
@Serializable
data class BusinessLocation(

    /** Address of the business */
    @SerialName("address") val address: String,

    /** Location of the business */
    @SerialName("location") val location: Location? = null,
)
