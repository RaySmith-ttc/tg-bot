package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a clickable area on a story media. */
@Serializable
data class StoryArea(

    /** Position of the area */
    @SerialName("position") val position: StoryAreaPosition,

    /** Type of the area */
    @SerialName("type") val type: StoryAreaType,
)
