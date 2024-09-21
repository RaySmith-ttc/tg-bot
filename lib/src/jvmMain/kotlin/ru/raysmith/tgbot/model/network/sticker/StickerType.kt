package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StickerType {
    @SerialName("regular") REGULAR,
    @SerialName("mask") MASK,
    @SerialName("custom_emoji") CUSTOM_EMOJI
}