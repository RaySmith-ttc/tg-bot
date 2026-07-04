package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VerticalAlignment {
    @SerialName("top") TOP,
    @SerialName("middle") MIDDLE,
    @SerialName("bottom") BOTTOM,
}
