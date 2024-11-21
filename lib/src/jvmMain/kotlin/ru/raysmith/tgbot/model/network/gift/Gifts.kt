package ru.raysmith.tgbot.model.network.gift

import kotlinx.serialization.SerialName

/** This object represent a list of gifts. */
data class Gifts(

    /** The list of gifts */
    @SerialName("gifts") val gifts: List<Gift>
)