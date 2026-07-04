package ru.raysmith.tgbot.model.api.message.reaction

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.ReactionTypeSerializer

/**
 * This object describes the type of a reaction. Currently, it can be one of
 *
 * - [ReactionTypeEmoji]
 * - [ReactionTypeCustomEmoji]
 * - [ReactionTypePaid]
 * */
@Serializable(ReactionTypeSerializer::class)
sealed interface ReactionType {

    /** Type of the reaction */
    val type: String
}
