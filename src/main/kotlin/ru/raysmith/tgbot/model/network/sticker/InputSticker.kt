package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.raysmith.tgbot.model.network.media.input.InputFile

/** This object describes a sticker to be added to a sticker set. */
data class InputSticker(
    @Transient val sticker: InputFile, // TODO docs
    @SerialName("emoji_list") val emojiList: List<String>,
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,
    @SerialName("keywords") val keywords: List<String>? = null
) {
    internal fun toSerializable(media: (InputFile) -> String) =
        SerializableInputSticker(media(sticker), emojiList, maskPosition, keywords)
}

@Serializable
internal data class SerializableInputSticker(
    @SerialName("sticker") val sticker: String,
    @SerialName("emoji_list") val emojiList: List<String>,
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,
    @SerialName("keywords") val keywords: List<String>? = null
)