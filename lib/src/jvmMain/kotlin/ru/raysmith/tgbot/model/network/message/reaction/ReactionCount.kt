package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents a reaction added to a message along with the number of times it was added. */
@Serializable
data class ReactionCount(

    /** Type of the reaction */
    @SerialName("type") val type: ReactionType,

    /** Number of times the reaction was added */
    @SerialName("total_count") val totalCount: Int
)
