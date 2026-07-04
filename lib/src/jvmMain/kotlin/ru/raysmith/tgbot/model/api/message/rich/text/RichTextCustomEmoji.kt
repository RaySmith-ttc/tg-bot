package ru.raysmith.tgbot.model.api.message.rich.text

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.API

/** A custom emoji. */
@Serializable
data class RichTextCustomEmoji(

    /**
     * Unique identifier of the custom emoji. Use [getCustomEmojiStickers][API.getCustomEmojiStickers] to get full
     * information about the sticker.
     * */
    @SerialName("custom_emoji_id") val customEmojiId: String,

    /** Alternative emoji for the custom emoji */
    @SerialName("alternative_text") val alternativeText: String,
) : RichText {

    /** Type of the rich text, always “custom_emoji” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "custom_emoji"
}
