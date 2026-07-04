package ru.raysmith.tgbot.model.api.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.reaction.ReactionType

/**
 * Describes a story area pointing to a suggested reaction.
 * Currently, a story can have up to 5 suggested reaction areas.
 * */
@Serializable
data class StoryAreaTypeSuggestedReaction(

    /** Type of the reaction */
    @SerialName("reaction_type") val reactionType: ReactionType,

    /** Pass *True* if the reaction area has a dark background */
    @SerialName("is_dark") val isDark: Boolean? = null,

    /** Pass *True* if reaction area corner is flipped */
    @SerialName("is_flipped") val isFlipped: Boolean? = null,
) : StoryAreaType {

    /** Type of the area, always “suggested_reaction” */
    override val type: String = "suggested_reaction"
}

