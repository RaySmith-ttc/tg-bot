package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes a story area containing weather information. Currently, a story can have up to 3 weather areas. */
@Serializable
data class StoryAreaTypeWeather(

    /** Temperature, in degree Celsius */
    @SerialName("temperature") val temperature: Float,

    /** Emoji representing the weather */
    @SerialName("emoji") val emoji: String,

    /** A color of the area background in the ARGB format */
    @SerialName("background_color") val backgroundColor: Int,
) : StoryAreaType {

    /** Type of the area, always “weather” */
    override val type: String = "weather"
}
