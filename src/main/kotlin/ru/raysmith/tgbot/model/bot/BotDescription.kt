package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents the bot's description. */
@Serializable
data class BotDescription(

    /** The bot's description */
    @SerialName("description") val description: String
)