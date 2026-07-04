package ru.raysmith.tgbot.model.api.message.reaction

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The reaction is based on a custom emoji. */
@Serializable
data class ReactionTypeCustomEmoji(

    /** Custom emoji identifier */
    @SerialName("custom_emoji_id") val customEmojiId: String
) : ReactionType {

    /** Type of the reaction, always “custom_emoji” */
    @EncodeDefault
    @SerialName("type") override val type: String = "custom_emoji"
}
