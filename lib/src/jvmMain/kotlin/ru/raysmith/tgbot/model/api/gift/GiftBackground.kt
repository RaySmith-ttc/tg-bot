package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the background of a gift. */
@Serializable
data class GiftBackground(

    /** Center color of the background in RGB format */
    @SerialName("center_color") val centerColor: Int,

    /** Edge color of the background in RGB format */
    @SerialName("edge_color") val edgeColor: Int,

    /** Text color of the background in RGB format */
    @SerialName("text_color") val textColor: Int,
)
