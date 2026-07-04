package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputStoryContentSerializer

/**
 * This object describes the content of a story to post. Currently, it can be one of
 *
 * - [InputStoryContentPhoto]
 * - [InputStoryContentVideo]
 * */
@Serializable(InputStoryContentSerializer::class)
sealed interface InputStoryContent {

    /** Type of the content */
    val type: String
}
