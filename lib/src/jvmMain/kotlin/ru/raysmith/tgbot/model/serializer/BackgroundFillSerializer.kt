package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.chat.background.fill.BackgroundFill
import ru.raysmith.tgbot.model.api.chat.background.fill.BackgroundFillFreeformGradient
import ru.raysmith.tgbot.model.api.chat.background.fill.BackgroundFillGradient
import ru.raysmith.tgbot.model.api.chat.background.fill.BackgroundFillSolid

internal object BackgroundFillSerializer : BaseJsonContentPolymorphicSerializer<BackgroundFill>(BackgroundFill::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "solid" -> BackgroundFillSolid.serializer()
        "gradient" -> BackgroundFillGradient.serializer()
        "freeform_gradient" -> BackgroundFillFreeformGradient.serializer()
        else -> null
    }
}
