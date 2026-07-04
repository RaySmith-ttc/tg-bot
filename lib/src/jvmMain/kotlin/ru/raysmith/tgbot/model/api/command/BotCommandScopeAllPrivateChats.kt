package ru.raysmith.tgbot.model.api.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [scope][BotCommandScope] of bot commands, covering all private chats. */
@Serializable
class BotCommandScopeAllPrivateChats : BotCommandScope {

    /** Scope type, must be *all_private_chats* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "all_private_chats"
}
