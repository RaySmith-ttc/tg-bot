package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an animated emoji that displays a random value. */
@Serializable
data class Dice(

    /** Emoji on which the dice throw animation is based */
    @SerialName("emoji") val emoji: String,

    /** Value of the dice, 1-6 for “🎲”, “🎯” and “🎳” base emoji, 1-5 for “🏀” and “⚽” base emoji, 1-64 for “🎰” base emoji */
    @SerialName("value") val value: Int

)