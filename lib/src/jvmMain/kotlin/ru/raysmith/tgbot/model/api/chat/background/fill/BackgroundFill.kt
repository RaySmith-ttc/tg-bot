package ru.raysmith.tgbot.model.api.chat.background.fill

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.BackgroundFillSerializer

// TODO test colors in subclasses, maybe create toColor() utils

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 *
 * - [BackgroundFillSolid]
 * - [BackgroundFillGradient]
 * - [BackgroundFillFreeformGradient]
 * */
@Serializable(BackgroundFillSerializer::class)
sealed interface BackgroundFill {

    /** Type of the background fill */
    val type: String
}
