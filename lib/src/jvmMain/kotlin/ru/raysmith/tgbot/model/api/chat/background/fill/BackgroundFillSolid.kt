package ru.raysmith.tgbot.model.api.chat.background.fill

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The background is filled using the selected color. */
@Serializable
data class BackgroundFillSolid(

    /** The color of the background fill in the RGB24 format */
    @SerialName("color") val color: Int,
) : BackgroundFill {

    /** Type of the background fill, always “solid” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "solid"
}
