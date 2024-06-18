package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a message about a scheduled giveaway. */
@Serializable
data class Giveaway(

    /** The list of chats which the user must join to participate in the giveaway */
    val chats: List<Chat>,

    /** Point in time (Unix timestamp) when winners of the giveaway will be selected */
    val winnersSelectionDate: Int,

    /** The number of users which are supposed to be selected as winners of the giveaway */
    val winnerCount: Int,

    /** *True*, if only users who join the chats after the giveaway started should be eligible to win */
    val onlyNewMembers: Boolean? = null,

    /** *True*, if the list of giveaway winners will be visible to everyone */
    val hasPublicWinners: Boolean? = null,

    /** Description of additional giveaway prize */
    val prizeDescription: String? = null,

    /**
     * A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes
     * indicating the countries from which eligible users for the giveaway must come. If empty, then all users can
     * participate in the giveaway. Users with a phone number that was bought on Fragment can always participate
     * in giveaways.
     * */
    val countryCodes: List<String>? = null,
)
