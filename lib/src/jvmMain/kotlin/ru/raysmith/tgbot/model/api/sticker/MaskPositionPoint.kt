package ru.raysmith.tgbot.model.api.sticker

import kotlinx.serialization.Serializable

@Serializable
enum class MaskPositionPoint {
    FOREHEAD, EYES, MONTH, CHIN
}
