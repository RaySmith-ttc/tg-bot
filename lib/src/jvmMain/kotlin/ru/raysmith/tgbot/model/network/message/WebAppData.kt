package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebAppData(
    @SerialName("data") val data: String,
    @SerialName("button_text") val buttonText: String
)