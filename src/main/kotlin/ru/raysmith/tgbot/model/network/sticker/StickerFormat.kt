package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Format of the sticker */
@Serializable
enum class StickerFormat {

    /** for a .WEBP or .PNG image */
    @SerialName("static") STATIC,

    /** for a .TGS animation */
    @SerialName("animated") ANIMATED,

    /** for a WEBM video */
    @SerialName("video") VIDEO
}