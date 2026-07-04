package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.chat.background.*

internal object BackgroundTypeSerializer :
    ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<BackgroundType>(BackgroundType::class, "type") {

    override fun selectDeserializer(type: String) = when (type) {
        "fill" -> BackgroundTypeFill.serializer()
        "wallpaper" -> BackgroundTypeWallpaper.serializer()
        "pattern" -> BackgroundTypePattern.serializer()
        "chat_theme" -> BackgroundTypeChatTheme.serializer()
        else -> null
    }
}
