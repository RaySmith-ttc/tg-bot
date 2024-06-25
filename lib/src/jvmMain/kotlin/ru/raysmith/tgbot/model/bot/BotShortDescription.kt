package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents the bot's short description. */
@Serializable
data class BotShortDescription(

    /** The bot's short description */
    @SerialName("short_description") val shortDescription: String
)