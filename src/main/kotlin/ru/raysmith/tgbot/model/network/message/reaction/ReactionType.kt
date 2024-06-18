package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.ReactionTypeSerializer

@Polymorphic
@Serializable(with = ReactionTypeSerializer::class)
sealed class ReactionType {

    /** Type of the reaction */
    abstract val type: String
}