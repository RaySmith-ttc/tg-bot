package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StickerFormat {
    @SerialName("static") STATIC,
    @SerialName("animated") ANIMATED,
    @SerialName("video") VIDEO
}