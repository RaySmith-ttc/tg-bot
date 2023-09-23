package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.PhotoSize

/** This object represents a sticker set. */
@Serializable
data class StickerSet(

    /** Sticker set name */
    @SerialName("name") val name: String,

    /** Sticker set title */
    @SerialName("title") val title: String,

    /** Type of stickers in the set */
    @SerialName("sticker_type") val stickerType: StickerType,

    /** *True*, if the sticker set contains [animated stickers](https://telegram.org/blog/animated-stickers) */
    @SerialName("is_animated") val isAnimated: Boolean,

    /** *True*, if the sticker set contains [video stickers](https://telegram.org/blog/video-stickers-better-reactions) */
    @SerialName("is_video") val isVideo: Boolean,

    /** List of all set stickers */
    @SerialName("stickers") val stickers: List<Sticker>,

    /** Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format */
    @SerialName("thumbnail") val thumbnail: PhotoSize? = null,
)

