package ru.raysmith.tgbot.model.network.chat.background

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.BackgroundTypeSerializer

/**
 * This object describes the type of a background. Currently, it can be one of
 *
 * - [BackgroundTypeFill]
 * - [BackgroundTypeWallpaper]
 * - [BackgroundTypePattern]
 * - [BackgroundTypeChatTheme]
 * */
@Serializable(with = BackgroundTypeSerializer::class)
sealed class BackgroundType {

    /** Type of the background */
    abstract val type: String
}