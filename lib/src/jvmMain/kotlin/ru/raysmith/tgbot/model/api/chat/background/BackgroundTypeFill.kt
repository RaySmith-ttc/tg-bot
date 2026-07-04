package ru.raysmith.tgbot.model.api.chat.background

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.chat.background.fill.BackgroundFill

/** The background is automatically filled based on the selected colors. */
@Serializable
data class BackgroundTypeFill(

    /** The background fill */
    @SerialName("fill") val fill: BackgroundFill,
) : BackgroundType {

    /** Type of the background, always “fill” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "fill"
}
