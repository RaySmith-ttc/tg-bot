package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.LocationAddress

/** Describes a story area pointing to a location. Currently, a story can have up to 10 location areas. */
@Serializable
data class StoryAreaTypeLocation(

    /** Location latitude in degrees */
    @SerialName("latitude") val latitude: Float,

    /** Location longitude in degrees */
    @SerialName("longitude") val longitude: Float,

    /** Address of the location */
    @SerialName("address") val address: LocationAddress? = null,
) : StoryAreaType {

    /** Type of the area, always “location” */
    override val type: String = "location"
}
