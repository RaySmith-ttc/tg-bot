package ru.raysmith.tgbot.model.network.giveaway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the creation of a scheduled giveaway. Currently holds no information.
 * */
@Serializable
data class GiveawayCreated(

    /** The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only */
    @SerialName("prize_star_count")
    val prizeStarCount: Int? = null,
)