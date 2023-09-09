package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [content][InputMessageContent] of a venue message to be sent as the result of an inline query. */
@Serializable
data class InputVenueMessageContent(

    /** Latitude of the venue in degrees */
    @SerialName("latitude") val latitude: Float,

    /** Longitude of the venue in degrees */
    @SerialName("longitude") val longitude: Float,

    /** Name of the venue */
    @SerialName("title") val title: String,

    /** Address of the venue */
    @SerialName("address") val address: String,

    /** Foursquare identifier of the venue, if known */
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
) : InputMessageContent()