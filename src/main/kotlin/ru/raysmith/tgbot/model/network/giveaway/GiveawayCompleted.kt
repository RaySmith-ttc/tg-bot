package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

/** This object represents a service message about the completion of a giveaway without public winners. */
@Serializable
data class GiveawayCompleted(

    /** Number of winners in the giveaway */
    val winnerCount: Int,

    /** Number of undistributed prizes */
    val unclaimedPrizeCount: Int? = null,

    /** Message with the giveaway that was completed, if it wasn't deleted */
    val giveawayMessage: Message? = null
)
