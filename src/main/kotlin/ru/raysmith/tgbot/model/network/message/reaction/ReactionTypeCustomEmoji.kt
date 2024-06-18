package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

/** The reaction is based on a custom emoji. */
@Serializable
data class ReactionTypeCustomEmoji(
    /** Custom emoji identifier */
    val customEmojiId: String

) : ReactionType() {

    @EncodeDefault
    /** Type of the reaction, always “custom_emoji” */
    override val type: String = "custom_emoji"

}
