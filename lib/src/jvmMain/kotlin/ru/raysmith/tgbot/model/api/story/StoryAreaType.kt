package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.StoryAreaTypeSerializer

/**
 * Describes the type of a clickable area on a story. Currently, it can be one of
 *
 * - [StoryAreaTypeLocation]
 * - [StoryAreaTypeSuggestedReaction]
 * - [StoryAreaTypeLink]
 * - [StoryAreaTypeWeather]
 * - [StoryAreaTypeUniqueGift]
 * */
@Serializable(StoryAreaTypeSerializer::class)
sealed interface StoryAreaType {

    /** Type of the area */
    val type: String
}
