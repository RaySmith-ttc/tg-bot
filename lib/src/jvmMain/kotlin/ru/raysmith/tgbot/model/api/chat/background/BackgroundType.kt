package ru.raysmith.tgbot.model.api.chat.background

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.BackgroundTypeSerializer

/**
 * This object describes the type of a background. Currently, it can be one of
 *
 * - [BackgroundTypeFill]
 * - [BackgroundTypeWallpaper]
 * - [BackgroundTypePattern]
 * - [BackgroundTypeChatTheme]
 * */
@Serializable(BackgroundTypeSerializer::class)
sealed interface BackgroundType {

    /** Type of the background */
    val type: String
}
