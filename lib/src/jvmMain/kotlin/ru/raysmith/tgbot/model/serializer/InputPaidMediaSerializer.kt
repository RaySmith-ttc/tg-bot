package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import ru.raysmith.tgbot.model.api.media.input.InputPaidMedia
import ru.raysmith.tgbot.model.api.media.input.InputPaidMediaLivePhoto
import ru.raysmith.tgbot.model.api.media.input.InputPaidMediaPhoto
import ru.raysmith.tgbot.model.api.media.input.InputPaidMediaVideo

internal object InputPaidMediaSerializer : BaseJsonContentPolymorphicSerializer<InputPaidMedia>(InputPaidMedia::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "live_photo" -> InputPaidMediaLivePhoto.serializer()
        "photo" -> InputPaidMediaPhoto.serializer()
        "video" -> InputPaidMediaVideo.serializer()
        else -> null
    } as KSerializer<out InputPaidMedia>
}
