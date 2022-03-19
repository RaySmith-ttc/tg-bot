package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [scope][BotCommandScope] of bot commands, covering all administrators of a specific group or supergroup chat. */
@Serializable
data class BotCommandScopeChatAdministrators(

    /** Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`) */
    @SerialName("chat_id")
    val chatId: String
): BotCommandScope() {

    /** Scope type, must be *chat_administrators* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "chat_administrators"
}