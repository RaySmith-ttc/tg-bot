package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.Serializable

@Serializable
enum class MaskPositionPoint {
    FOREHEAD, EYES, MONTH, CHIN
}