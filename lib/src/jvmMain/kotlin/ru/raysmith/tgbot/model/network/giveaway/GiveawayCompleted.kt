package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.Message

/** This object represents a service message about the completion of a giveaway without public winners. */
@Serializable
data class GiveawayCompleted(

    /** Number of winners in the giveaway */
    @SerialName("winner_count") val winnerCount: Int,

    /** Number of undistributed prizes */
    @SerialName("unclaimed_prize_count") val unclaimedPrizeCount: Int? = null,

    /** Message with the giveaway that was completed, if it wasn't deleted */
    @SerialName("giveaway_message") val giveawayMessage: Message? = null
)
