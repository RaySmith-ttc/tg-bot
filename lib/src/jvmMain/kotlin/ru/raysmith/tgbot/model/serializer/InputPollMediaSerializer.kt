package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.input.*

internal object InputPollMediaSerializer : BaseJsonContentPolymorphicSerializer<InputPollMedia>(InputPollMedia::class) {
    override fun selectDeserializer(type: String) = when (type) {
        "animation" -> InputMediaAnimation.serializer()
        "audio" -> InputMediaAudio.serializer()
        "document" -> InputMediaDocument.serializer()
        "live_photo" -> InputMediaLivePhoto.serializer()
        "location" -> InputMediaLocation.serializer()
        "photo" -> InputMediaPhoto.serializer()
        "venue" -> InputMediaVenue.serializer()
        "video" -> InputMediaVideo.serializer()
        else -> null
    }
}
