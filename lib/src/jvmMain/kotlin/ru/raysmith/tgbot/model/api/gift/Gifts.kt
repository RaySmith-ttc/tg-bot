package ru.raysmith.tgbot.model.api.gift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represent a list of gifts. */
@Serializable
data class Gifts(

    /** The list of gifts */
    @SerialName("gifts") val gifts: List<Gift>,
)
