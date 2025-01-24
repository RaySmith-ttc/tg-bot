package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/** Represents a link to an article or web page. */
@Serializable
data class InlineQueryResultArticle(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Title of the result */
    @SerialName("title") val title: String,

    /** Content of the message to be sent */
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

    /** URL of the result */
    @SerialName("url") val url: String? = null,

    /** Short description of the result */
    @SerialName("description") val description: String? = null,

    /** Url of the thumbnail for the result */
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,

    /** Thumbnail width */
    @SerialName("thumbnail_width") val thumbnailWidth: Int? = null,

    /** Thumbnail height */
    @SerialName("thumbnail_height") val thumbnailHeight: Int? = null
) : InlineQueryResult() {

    /** Type of the result, must be *article* */
    @EncodeDefault
    override val type: String = "article"
}

