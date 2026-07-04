package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.paid.PaidMedia
import ru.raysmith.tgbot.model.api.media.paid.PaidMediaPhoto
import ru.raysmith.tgbot.model.api.media.paid.PaidMediaPreview
import ru.raysmith.tgbot.model.api.media.paid.PaidMediaVideo

internal object PaidMediaSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<PaidMedia>(PaidMedia::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "preview" -> PaidMediaPreview.serializer()
        "photo" -> PaidMediaPhoto.serializer()
        "video" -> PaidMediaVideo.serializer()
        else -> null
    }
}
