package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.file.File
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
    @SerialName("type") val type: StickerType,

    /** Sticker width */
    @SerialName("width") val width: Int,

    /** Sticker height */
    @SerialName("height") val height: Int,

    /** *True*, if the sticker is [animated](https://telegram.org/blog/animated-stickers) */
    @SerialName("is_animated") val isAnimated: Boolean,

    /** *True*, if the sticker is a video sticker */
    @SerialName("is_video") val isVideo: Boolean,

    /** Sticker thumbnail in the .WEBP or .JPG format */
    @SerialName("thumbnail") val thumbnail: PhotoSize? = null,

    /** Emoji associated with the sticker */
    @SerialName("emoji") val emoji: String? = null,

    /** Name of the sticker set to which the sticker belongs */
    @SerialName("set_name") val setName: String? = null,

    /** For premium regular stickers, premium animation for the sticker */
    @SerialName("premium_animation") val premiumAnimation: File? = null,

    /** For mask stickers, the position where the mask should be placed */
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,

    /** For custom emoji stickers, unique identifier of the custom emoji */
    @SerialName("custom_emoji_id") val customEmojiId: String? = null,

    /**
     * *True*, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge
     * in emoji status, white color on chat photos, or another appropriate color in other places
     * */
    @SerialName("needs_repainting") val needsRepainting: Boolean? = null,

    /** File size in bytes */
    @SerialName("file_size") val fileSize: Int? = null

)

