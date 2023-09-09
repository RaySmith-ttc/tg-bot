package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [content][InputMessageContent] of a location message to be sent as the result of an inline query. */
@Serializable
data class InputLocationMessageContent(

    /** Latitude of the location in degrees */
    @SerialName("latitude") val latitude: Float,

    /** Longitude of the location in degrees */
    @SerialName("longitude") val longitude: Float,

    /** The radius of uncertainty for the location, measured in meters; 0-1500 */
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,

    /** Period in seconds for which the location can be updated, should be between 60 and 86400. */
    @SerialName("live_period") val livePeriod: Int? = null,

    /** For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified. */
    @SerialName("heading") val heading: Int? = null,

    /**
     * For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
     * Must be between 1 and 100000 if specified.
     * */
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null,
) : InputMessageContent()