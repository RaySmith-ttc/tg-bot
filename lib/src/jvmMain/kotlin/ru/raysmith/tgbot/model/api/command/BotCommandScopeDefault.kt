package ru.raysmith.tgbot.model.api.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the default [scope][BotCommandScope] of bot commands. Default commands are used if no commands with a
 * narrower scope are specified for the user.
 * */
@Serializable
class BotCommandScopeDefault : BotCommandScope {

    /** Scope type, must be *default* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "default"
}
