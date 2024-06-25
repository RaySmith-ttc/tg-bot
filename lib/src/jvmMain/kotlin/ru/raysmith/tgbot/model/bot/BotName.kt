package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents the bot's name. */
@Serializable
data class BotName(

    /** The bot's name */
    @SerialName("name") val name: String
)