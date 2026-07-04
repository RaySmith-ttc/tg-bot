package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Formatted date and time. */
@Serializable
data class RichTextDateTime(

    /** The text */
    @SerialName("text") val text: RichText,

    /** The Unix time associated with the entity */
    @SerialName("unix_time") val unixTime: Int,

    /**
     * The string that defines the formatting of the date and time. See
     * [date-time entity formatting](https://core.telegram.org/bots/api#date-time-entity-formatting) for more details
     * */
    @SerialName("date_time_format") val dateTimeFormat: String,
) : RichText {

    /** Type of the rich text, always “date_time” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "date_time"
}
