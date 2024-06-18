package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.Serializable

/** Represents a reaction added to a message along with the number of times it was added. */
@Serializable
data class ReactionCount(

    /** Type of the reaction */
    val type: ReactionType,

    /** Number of times the reaction was added */
    val totalCount: Int
)
