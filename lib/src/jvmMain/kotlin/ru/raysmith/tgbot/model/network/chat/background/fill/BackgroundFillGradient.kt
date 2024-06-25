package ru.raysmith.tgbot.model.network.chat.background.fill

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The background is a gradient fill. */
@Serializable
data class BackgroundFillGradient(

    /** Top color of the gradient in the RGB24 format */
    @SerialName("top_color") val topColor: Int,

    /** Bottom color of the gradient in the RGB24 format */
    @SerialName("bottom_color") val bottomColor: Int,

    /** Clockwise rotation angle of the background fill in degrees; 0-359 */
    @SerialName("rotation_angle") val rotationAngle: Int,
) : BackgroundFill() {

    /** Type of the background fill, always “gradient” */
    override val type: String = "gradient"
}
