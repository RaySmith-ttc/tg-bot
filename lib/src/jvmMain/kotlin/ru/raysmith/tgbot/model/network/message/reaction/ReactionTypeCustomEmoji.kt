package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The reaction is based on a custom emoji. */
@Serializable
data class ReactionTypeCustomEmoji(
    /** Custom emoji identifier */
    @SerialName("custom_emoji_id") val customEmojiId: String

) : ReactionType() {

    @EncodeDefault
    /** Type of the reaction, always “custom_emoji” */
    override val type: String = "custom_emoji"

}

/** The reaction is paid. */
@Serializable
data object ReactionTypePaid : ReactionType() {

    @EncodeDefault
    /** Type of the reaction, always “paid” */
    override val type: String = "paid"

}
