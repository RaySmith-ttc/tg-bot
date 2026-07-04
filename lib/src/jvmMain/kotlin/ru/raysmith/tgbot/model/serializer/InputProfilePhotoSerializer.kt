package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.profile.InputProfilePhoto
import ru.raysmith.tgbot.model.api.profile.InputProfilePhotoAnimated
import ru.raysmith.tgbot.model.api.profile.InputProfilePhotoStatic

internal object InputProfilePhotoSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<InputProfilePhoto>(InputProfilePhoto::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "static" -> InputProfilePhotoStatic.serializer()
        "animated" -> InputProfilePhotoAnimated.serializer()
        else -> null
    }
}


