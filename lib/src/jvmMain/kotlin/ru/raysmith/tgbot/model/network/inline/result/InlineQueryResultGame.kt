package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup

/** Represents a [Game](https://core.telegram.org/bots/api#games). */
@Serializable
data class InlineQueryResultGame(

    /** Unique identifier for this result, 1-64 Bytes */
    @SerialName("id") val id: String,

    /** Short name of the game */
    @SerialName("game_short_name") val gameShortName: String,

    /** [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,

) : InlineQueryResult() {

    /** Type of the result, must be *game* */
    @EncodeDefault
    override val type: String = "game"
}