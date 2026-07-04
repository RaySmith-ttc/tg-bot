package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.inline.result.*

internal object InlineQueryResultSerializer : BaseJsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {

    // not cached version are commented out because it's not possible to receive objects with url
    override fun selectDeserializer(type: String) = when(type) {
        "audio" -> InlineQueryResultCachedAudio.serializer()
        "document" -> InlineQueryResultCachedDocument.serializer()
        "gif" -> InlineQueryResultCachedGif.serializer()
        "mpeg4_gif" -> InlineQueryResultCachedMpeg4Gif.serializer()
        "photo" -> InlineQueryResultCachedPhoto.serializer()
        "sticker" -> InlineQueryResultCachedSticker.serializer()
        "video" -> InlineQueryResultCachedVideo.serializer()
        "voice" -> InlineQueryResultCachedVoice.serializer()
        "article" -> InlineQueryResultArticle.serializer()
//        "audio" -> InlineQueryResultAudio.serializer()
        "contact" -> InlineQueryResultContact.serializer()
        "game" -> InlineQueryResultGame.serializer()
//        "document" -> InlineQueryResultDocument.serializer()
//        "gif" -> InlineQueryResultGif.serializer()
        "location" -> InlineQueryResultLocation.serializer()
//        "mpeg4_gif" -> InlineQueryResultMpeg4Gif.serializer()
//        "photo" -> InlineQueryResultPhoto.serializer()
        "venue" -> InlineQueryResultVenue.serializer()
//        "video" -> InlineQueryResultVideo.serializer()
//        "voice" -> InlineQueryResultVoice.serializer()
        else -> null
    }
}
