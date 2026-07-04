package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes the position of a clickable area within a story. */
@Serializable
data class StoryAreaPosition(

    /** The abscissa of the area's center, as a percentage of the media width */
    @SerialName("x_percentage") val xPercentage: Float,

    /** The ordinate of the area's center, as a percentage of the media height */
    @SerialName("y_percentage") val yPercentage: Float,

    /** The width of the area's rectangle, as a percentage of the media width */
    @SerialName("width_percentage") val widthPercentage: Float,

    /** The height of the area's rectangle, as a percentage of the media height */
    @SerialName("height_percentage") val heightPercentage: Float,

    /** The clockwise rotation angle of the rectangle, in degrees; 0-360 */
    @SerialName("rotation_angle") val rotationAngle: Float,

    /** The radius of the rectangle corner rounding, as a percentage of the media width */
    @SerialName("corner_radius_percentage") val cornerRadiusPercentage: Float,
)
