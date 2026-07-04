package ru.raysmith.tgbot.model.api.message.reaction

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The reaction is paid. */
@Serializable
data object ReactionTypePaid : ReactionType {

    /** Type of the reaction, always “paid” */
    @EncodeDefault
    @SerialName("type") override val type: String = "paid"
}
