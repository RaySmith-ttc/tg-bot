package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object describes the position on faces where a mask should be placed by default. */
data class MaskPosition(

    /** The part of the face relative to which the mask should be placed */
    @SerialName("point") val point: MaskPositionPoint,

    /**
     * Shift by X-axis measured in widths of the mask scaled to the face size, from left to right.
     * For example, choosing -1.0 will place mask just to the left of the default mask position.
     * */
    @SerialName("x_shift") val x_shift: Float,

    /**
     * Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom.
     * For example, 1.0 will place the mask just below the default mask position.
     * */
    @SerialName("y_shift") val y_shift: Float,

    /** Mask scaling coefficient. For example, 2.0 means double size. */
    @SerialName("scale") val scale: Float,
)

