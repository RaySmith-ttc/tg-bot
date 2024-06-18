package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

/** Represents the [scope][BotCommandScope] of bot commands, covering all group and supergroup chat administrators. */
@Serializable
class BotCommandScopeAllChatAdministrators : BotCommandScope() {

    /** Scope type, must be *all_chat_administrators* */
    @EncodeDefault
    override val type: String = "all_chat_administrators"
}