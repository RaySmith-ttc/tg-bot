package ru.raysmith.tgbot.model.network.chat.background.fill

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.BackgroundFillSerializer

// TODO test colors in subclasses, maybe create toColor() utils

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 *
 * - [BackgroundFillSolid]
 * - [BackgroundFillGradient]
 * - [BackgroundFillFreeformGradient]
 * */
@Serializable(with = BackgroundFillSerializer::class)
sealed class BackgroundFill {

    /** Type of the background fill */
    abstract val type: String
}