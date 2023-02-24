package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.PhotoSize
import ru.raysmith.tgbot.model.network.message.MaskPosition

@Serializable
/** This object represents a sticker. */
data class Sticker(

    /** Identifier for this file, which can be used to download or reuse the file */
    @SerialName("file_id") val fileId: String,

    /** Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file. */
    @SerialName("file_unique_id") val fileUniqueId: String,

    /** Sticker width */
    @SerialName("width") val width: Int,

    /** Sticker height */
    @SerialName("height") val height: Int,

    /** True, if the sticker is [animated](https://telegram.org/blog/animated-stickers) */
    @SerialName("is_animated") val isAnimated: Boolean,

    /** True, if the sticker is a video sticker */
    @SerialName("is_video") val isVideo: Boolean,

    /** Sticker thumbnail in the .WEBP or .JPG format */
    @SerialName("thumb") val thumb: PhotoSize? = null,

    /** Emoji associated with the sticker */
    @SerialName("emoji") val emoji: String? = null,

    /** Name of the sticker set to which the sticker belongs */
    @SerialName("set_name") val setName: String? = null,

    /** For mask stickers, the position where the mask should be placed */
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,

    /** File size */
    @SerialName("file_size") val fileSize: Int? = null

)

