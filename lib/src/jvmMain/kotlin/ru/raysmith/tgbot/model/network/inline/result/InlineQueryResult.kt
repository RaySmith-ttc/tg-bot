package ru.raysmith.tgbot.model.network.inline.result

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * This object represents one result of an inline query.
 * Telegram clients currently support results of the following 20 types:
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
@Polymorphic
@Serializable
sealed class InlineQueryResult {
    abstract val type: String
}