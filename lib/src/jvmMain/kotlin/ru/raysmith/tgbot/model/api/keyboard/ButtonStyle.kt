package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ButtonStyle {

    /** red */
    @SerialName("danger")
    DANGER,

    /** green */
    @SerialName("success")
    SUCCESS,

    /** blue */
    @SerialName("primary")
    PRIMARY,
}
