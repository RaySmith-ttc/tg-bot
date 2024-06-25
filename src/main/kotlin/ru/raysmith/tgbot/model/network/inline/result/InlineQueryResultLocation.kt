package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.message.LivePeriod
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use
 * *[inputMessageContent]* to send a message with the specified content instead of the location.
 * */
@Serializable
data class InlineQueryResultLocation(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Location latitude in degrees */
    @SerialName("latitude") val latitude: Float,

    /** Location longitude in degrees */
    @SerialName("longitude") val longitude: Float,

    /** Location title */
    @SerialName("title") val title: String,

    /** The radius of uncertainty for the location, measured in meters; 0-1500 */
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,

    /**
     * Period in seconds during which the location can be updated, should be between 60 and 86400 or
     * [LivePeriod.Indefinitely] for live locations that can be edited indefinitely.
     * */
    @SerialName("live_period") val livePeriod: LivePeriod? = null,

    /**
     * For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     * */
    @SerialName("heading") val heading: Int? = null,

    /**
     * For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
     * Must be between 1 and 100000 if specified.
     * */
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Int? = null,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** Content of the message to be sent instead of the location */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,

    /** Url of the thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Thumbnail width */
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    /** Thumbnail height */
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null,

    ) : InlineQueryResult() {

    /** Type of the result, must be *location* */
    @EncodeDefault
    override val type: String = "location"
}