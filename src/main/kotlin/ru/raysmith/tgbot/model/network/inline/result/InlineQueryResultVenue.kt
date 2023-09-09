package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/**
 * Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use
 * *[inputMessageContent]* to send a message with the specified content instead of the venue.
 * */
@Serializable
data class InlineQueryResultVenue(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Latitude of the venue location in degrees */
    @SerialName("latitude") val latitude: Float,

    /** Longitude of the venue location in degrees */
    @SerialName("longitude") val longitude: Float,

    /** Title of the venue */
    @SerialName("title") val title: String,

    /** Address of the venue */
    @SerialName("address") val address: String,

    /** Foursquare identifier of the venue if known */
    @SerialName("foursquare_id") val foursquareId: String? = null,

    /** Foursquare type of the venue. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.) */
    @SerialName("foursquare_type") val foursquareType: String? = null,

    /** Google Places identifier of the venue */
    @SerialName("google_place_id") val googlePlaceId: String? = null,

    /** Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).) */
    @SerialName("google_place_type") val googlePlaceType: String? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the venue */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    /** Url of the thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Thumbnail width */
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    /** Thumbnail height */
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *venue* */
    @EncodeDefault
    override val type: String = "venue"
}