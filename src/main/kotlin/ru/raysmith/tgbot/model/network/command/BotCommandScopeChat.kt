package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.*
import ru.raysmith.tgbot.network.TelegramApi

/** Represents the [scope][BotCommandScope] of bot commands, covering a specific chat. */
@Serializable
data class BotCommandScopeChat(

    /** Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`) */
    @SerialName("chat_id")
    val chatId: String
): BotCommandScope() {

    /** Scope type, must be *chat* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "chat"
}