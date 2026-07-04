package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the colors of the backdrop of a unique gift. */
@Serializable
data class UniqueGiftBackdropColors(

    /** The color in the center of the backdrop in RGB format */
    @SerialName("center_color") val centerColor: Int,

    /** The color on the edges of the backdrop in RGB format */
    @SerialName("edge_color") val edgeColor: Int,

    /** The color to be applied to the symbol in RGB format */
    @SerialName("symbol_color") val symbolColor: Int,

    /** The color for the text on the backdrop in RGB format */
    @SerialName("text_color") val textColor: Int,
)
