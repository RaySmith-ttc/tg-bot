package ru.raysmith.tgbot.model.network.updates.boost

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User

/**
 * The boost was obtained by the creation of a Telegram Premium giveaway.
 * This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 * */
@Serializable
data class ChatBoostSourceGiveaway(

    /**
     * Identifier of a message in the chat with the giveaway; the message could have been deleted already.
     * May be 0 if the message isn't sent yet.
     * */
    val giveawayMessageId: Int,

    /** User that boosted the chat */
    val user: User? = null,

    /** True, if the giveaway was completed, but there was no user to win the prize */
    val isUnclaimed: Boolean
) : ChatBoostSource() {

    /** Source of the boost, always “giveaway” */
    override val source: String = "giveaway"
}