package ru.raysmith.tgbot.model.network.chat.background

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.background.fill.BackgroundFill
import ru.raysmith.tgbot.model.network.media.Document

/**
 * The background is a PNG or TGV (gzipped subset of SVG with MIME type “application/x-tgwallpattern”) pattern to be
 * combined with the background fill chosen by the user.
 * */
@Serializable
data class BackgroundTypePattern(

    /** Document with the pattern */
    @SerialName("document") val document: Document,

    /** The background fill that is combined with the pattern */
    @SerialName("fill") val fill: BackgroundFill,

    /** Intensity of the pattern when it is shown above the filled background; 0-100 */
    @SerialName("intensity") val intensity: Int,

    /**
     * *True*, if the background fill must be applied only to the pattern itself. All other pixels are black in
     * this case. For dark themes only
     * */
    @SerialName("is_inverted") val isInverted: Boolean? = null,

    /** *True*, if the background moves slightly when the device is tilted */
    @SerialName("is_moving") val isMoving: Boolean? = null,
) : BackgroundType() {

    /** Type of the background, always “pattern” */
    override val type: String = "pattern"
}