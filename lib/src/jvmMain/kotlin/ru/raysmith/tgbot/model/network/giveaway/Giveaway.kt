package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a message about a scheduled giveaway. */
@Serializable
data class Giveaway(

    /** The list of chats which the user must join to participate in the giveaway */
    @SerialName("chats") val chats: List<Chat>,

    /** Point in time (Unix timestamp) when winners of the giveaway will be selected */
    @SerialName("winners_selection_date") val winnersSelectionDate: Int,

    /** The number of users which are supposed to be selected as winners of the giveaway */
    @SerialName("winner_count") val winnerCount: Int,

    /** *True*, if only users who join the chats after the giveaway started should be eligible to win */
    @SerialName("only_new_members") val onlyNewMembers: Boolean? = null,

    /** *True*, if the list of giveaway winners will be visible to everyone */
    @SerialName("has_public_winners") val hasPublicWinners: Boolean? = null,

    /** Description of additional giveaway prize */
    @SerialName("prize_description") val prizeDescription: String? = null,

    /**
     * A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes
     * indicating the countries from which eligible users for the giveaway must come. If empty, then all users can
     * participate in the giveaway. Users with a phone number that was bought on Fragment can always participate
     * in giveaways.
     * */
    @SerialName("country_codes") val countryCodes: List<String>? = null,

    /** The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only */
    @SerialName("prize_star_count") val prizeStarCount: Int? = null,
)