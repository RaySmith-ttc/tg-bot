package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a venue. */
@Serializable
data class Venue(

    /** Venue location. Can't be a live location */
    @SerialName("location") val location: Location,

    /** Name of the venue */
    @SerialName("title") val title: String,

    /** Address of the venue */
    @SerialName("address") val address: String,

    /** Foursquare identifier of the venue */
    @SerialName("foursquare_id") val foursquareId: String? = null,

    /** Foursquare type of the venue. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.) */
    @SerialName("foursquare_type") val foursquareType: String? = null,

    /** Google Places identifier of the venue */
    @SerialName("google_place_id") val googlePlaceId: String? = null,

    /** Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).) */
    @SerialName("google_place_type") val googlePlaceType: String? = null,
)

