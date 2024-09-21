package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/** Represents the default [scope][BotCommandScope] of bot commands. Default commands are used if no commands with a narrower scope are specified for the user. */
@Serializable
class BotCommandScopeDefault : BotCommandScope() {

    /** Scope type, must be *default* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "default"
}