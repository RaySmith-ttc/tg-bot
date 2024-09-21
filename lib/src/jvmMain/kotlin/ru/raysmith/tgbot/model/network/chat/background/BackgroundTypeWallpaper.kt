package ru.raysmith.tgbot.model.network.chat.background

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.Document

/** The background is a wallpaper in the JPEG format. */
@Serializable
data class BackgroundTypeWallpaper(

    /** Document with the wallpaper */
    @SerialName("document") val document: Document,

    /** Dimming of the background in dark themes, as a percentage; 0-100 */
    @SerialName("dark_theme_dimming") val darkThemeDimming: Int,

    /** *True*, if the wallpaper is downscaled to fit in a 450x450 square and then box-blurred with radius 12 */
    @SerialName("is_blurred") val isBlurred: Boolean? = null,

    /** *True*, if the background moves slightly when the device is tilted */
    @SerialName("is_moving") val isMoving: Boolean? = null,
) : BackgroundType() {

    /** Type of the background, always “wallpaper” */
    override val type: String = "wallpaper"
}