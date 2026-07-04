package ru.raysmith.tgbot.model.api.chat.background.fill

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
The background is a freeform gradient that rotates after every message in the chat. */
@Serializable
data class BackgroundFillFreeformGradient(

    /** A list of the 3 or 4 base colors that are used to generate the freeform gradient in the RGB24 format */
    @SerialName("colors") val colors: List<Int>,
) : BackgroundFill {

    /** Type of the background fill, always “freeform_gradient” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "freeform_gradient"
}
