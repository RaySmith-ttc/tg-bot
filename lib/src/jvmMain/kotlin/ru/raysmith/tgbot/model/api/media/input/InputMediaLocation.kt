package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents a location to be sent. */
@Serializable
data class InputMediaLocation(

    /** Latitude of the location */
    @SerialName("latitude") val latitude: Float,

    /** Longitude of the location */
    @SerialName("longitude") val longitude: Float,

    /** The radius of uncertainty for the location, measured in meters; 0-1500 */
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
) : InputPollMedia, InputPollOptionMedia {

    /** Type of the media, must be *location* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "location"
}
