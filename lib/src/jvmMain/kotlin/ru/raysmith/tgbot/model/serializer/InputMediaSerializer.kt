package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import ru.raysmith.tgbot.model.api.media.input.*

internal object InputMediaSerializer : BaseJsonContentPolymorphicSerializer<InputMedia>(InputMedia::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "animation" -> InputMediaAnimation.serializer()
        "audio" -> InputMediaAudio.serializer()
        "document" -> InputMediaDocument.serializer()
        "live_photo" -> InputMediaLivePhoto.serializer()
        "photo" -> InputMediaPhoto.serializer()
        "video" -> InputMediaVideo.serializer()
        else -> null
    } as KSerializer<out InputMedia>?
}
