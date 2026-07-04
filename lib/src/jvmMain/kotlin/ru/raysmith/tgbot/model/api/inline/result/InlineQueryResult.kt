package ru.raysmith.tgbot.model.api.inline.result

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InlineQueryResultSerializer

/**
 * This object represents one result of an inline query. Telegram clients currently support results of the following 20
 * types:
 *
 * - [InlineQueryResultCachedAudio]
 * - [InlineQueryResultCachedDocument]
 * - [InlineQueryResultCachedGif]
 * - [InlineQueryResultCachedMpeg4Gif]
 * - [InlineQueryResultCachedPhoto]
 * - [InlineQueryResultCachedSticker]
 * - [InlineQueryResultCachedVideo]
 * - [InlineQueryResultCachedVoice]
 * - [InlineQueryResultArticle]
 * - [InlineQueryResultAudio]
 * - [InlineQueryResultContact]
 * - [InlineQueryResultGame]
 * - [InlineQueryResultDocument]
 * - [InlineQueryResultGif]
 * - [InlineQueryResultLocation]
 * - [InlineQueryResultMpeg4Gif]
 * - [InlineQueryResultPhoto]
 * - [InlineQueryResultVenue]
 * - [InlineQueryResultVideo]
 * - [InlineQueryResultVoice]
 * */
@Serializable(InlineQueryResultSerializer::class)
sealed interface InlineQueryResult {
    val type: String
}
