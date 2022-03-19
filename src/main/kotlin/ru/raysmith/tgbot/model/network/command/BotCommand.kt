package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.Serializable


/** This object represents a bot command. */
@Serializable
data class BotCommand(

    /** Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores. */
    val command: String,

    /** Description of the command; 1-256 characters. */
    val description: String
)