package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.input.*

internal object InputPollOptionMediaSerializer :
    ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<InputPollOptionMedia>(InputPollOptionMedia::class) {

    override fun selectDeserializer(type: String) = when (type) {
        "animation" -> InputMediaAnimation.serializer()
        "link" -> InputMediaLink.serializer()
        "live_photo" -> InputMediaLivePhoto.serializer()
        "photo" -> InputMediaPhoto.serializer()
        "location" -> InputMediaLocation.serializer()
        "sticker" -> InputMediaSticker.serializer()
        "venue" -> InputMediaVenue.serializer()
        "video" -> InputMediaVideo.serializer()
        else -> null
    }
}
