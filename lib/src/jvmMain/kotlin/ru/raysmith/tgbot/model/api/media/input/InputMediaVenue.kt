package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents a venue to be sent. */
@Serializable
data class InputMediaVenue(

    /** Latitude of the location */
    @SerialName("latitude") val latitude: Float,

    /** Longitude of the location */
    @SerialName("longitude") val longitude: Float,

    /** Name of the venue */
    @SerialName("title") val title: String,

    /** Address of the venue */
    @SerialName("address") val address: String,

    /** Foursquare identifier of the venue */
    @SerialName("foursquare_id") val foursquareId: String? = null,

    /**
     * Foursquare type of the venue, if known.
     * (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     * */
    @SerialName("foursquare_type") val foursquareType: String? = null,

    /** Google Places identifier of the venue */
    @SerialName("google_place_id") val googlePlaceId: String? = null,

    /**
     * Google Places type of the venue.
     * (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     * */
    @SerialName("google_place_type") val googlePlaceType: String? = null,
) : InputPollMedia, InputPollOptionMedia {

    /** Type of the media, must be *venue* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "venue"
}
