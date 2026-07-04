package ru.raysmith.tgbot.model.api.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Format of the sticker
 *
 * - [STATIC] - **.WEBP** or **.PNG** image
 * - [ANIMATED] - **.TGS** animation
 * - [VIDEO] - **.WEBM** video
 * */
@Serializable
enum class StickerFormat {

    /** **.WEBP** or **.PNG** image */
    @SerialName("static") STATIC,

    /** **.TGS** animation */
    @SerialName("animated") ANIMATED,

    /** **.WEBM** video */
    @SerialName("video") VIDEO
}
