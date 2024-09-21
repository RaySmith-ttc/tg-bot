package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a message about the completion of a giveaway with public winners. */
@Serializable
data class GiveawayWinners(

    /** The chat that created the giveaway */
    @SerialName("chat") val chat: Chat,

    /** Identifier of the message with the giveaway in the chat */
    @SerialName("giveaway_message_id") val giveawayMessageId: Int,

    /** Point in time (Unix timestamp) when winners of the giveaway were selected */
    @SerialName("winners_selection_date") val winnersSelectionDate: Int,

    /** Total number of winners in the giveaway */
    @SerialName("winner_count") val winnerCount: Int,

    /** List of up to 100 winners of the giveaway */
    @SerialName("winners") val winners: List<User>,

    /** The number of other chats the user had to join in order to be eligible for the giveaway */
    @SerialName("additional_chat_count") val additionalChatCount: Int? = null,

    /** The number of Telegram Stars that were split between giveaway winners; for Telegram Star giveaways only */
    @SerialName("prize_star_count") val prizeStarCount: Int? = null,

    /**
     * The number of months the Telegram Premium subscription won from the giveaway will be active for;
     * for Telegram Premium giveaways only
     * */
    @SerialName("premium_subscription_month_count") val premiumSubscriptionMonthCount: Int? = null,

    /** Number of undistributed prizes */
    @SerialName("unclaimed_prize_count") val unclaimedPrizeCount: Int? = null,

    /** *True*, if only users who had joined the chats after the giveaway started were eligible to win */
    @SerialName("only_new_members") val onlyNewMembers: Boolean? = null,

    /** *True*, if the giveaway was canceled because the payment for it was refunded */
    @SerialName("was_refunded") val wasRefunded: Boolean? = null,

    /** Description of additional giveaway prize */
    @SerialName("prize_description") val prizeDescription: String? = null,

)
