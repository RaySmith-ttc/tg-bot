package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/** Represents the [scope][BotCommandScope] of bot commands, covering all group and supergroup chats. */
@Serializable
class BotCommandScopeAllGroupChats : BotCommandScope() {

    /** Scope type, must be *all_group_chats* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "all_group_chats"
}