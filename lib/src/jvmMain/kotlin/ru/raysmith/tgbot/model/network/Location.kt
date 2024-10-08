package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.message.LivePeriod

/** This object represents a point on the map. */
@Serializable
data class Location(

    /** Latitude as defined by the sender */
    @SerialName("latitude") val latitude: Float,

    /** Longitude as defined by the sender */
    @SerialName("longitude") val longitude: Float,

    /** The radius of uncertainty for the location, measured in meters; 0-1500 */
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,

    /** Time relative to the message sending date, during which the location can be updated, in seconds. For active live locations only. */
    @SerialName("live_period") val livePeriod: LivePeriod? = null,

    /** The direction in which user is moving, in degrees; 1-360. For active live locations only. */
    @SerialName("heading") val heading: Int? = null,

    /** Maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only. */
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null,
)

