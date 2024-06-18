package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a message about the completion of a giveaway with public winners. */
@Serializable
data class GiveawayWinners(

    /** The chat that created the giveaway */
    val chat: Chat,

    /** Identifier of the message with the giveaway in the chat */
    val giveawayMessageId: Int,

    /** Point in time (Unix timestamp) when winners of the giveaway were selected */
    val winnersSelectionDate: Int,

    /** Total number of winners in the giveaway */
    val winnerCount: Int,

    /** List of up to 100 winners of the giveaway */
    val winners: List<User>,

    /** The number of other chats the user had to join in order to be eligible for the giveaway */
    val additionalChatCount: Int? = null,

    /** The number of months the Telegram Premium subscription won from the giveaway will be active for */
    val premiumSubscriptionMonthCount: Int? = null,

    /** Number of undistributed prizes */
    val unclaimedPrizeCount: Int? = null,

    /** *True*, if only users who had joined the chats after the giveaway started were eligible to win */
    val onlyNewMembers: Boolean? = null,

    /** *True*, if the giveaway was canceled because the payment for it was refunded */
    val wasRefunded: Boolean? = null,

    /** Description of additional giveaway prize */
    val prizeDescription: String? = null,

)
