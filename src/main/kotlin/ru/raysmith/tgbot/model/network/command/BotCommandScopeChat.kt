package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.network.serializer.ChatIdSerializer

/** Represents the [scope][BotCommandScope] of bot commands, covering a specific chat. */
@Serializable
data class BotCommandScopeChat(

    /** Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`) */
    @SerialName("chat_id")
    @Serializable(with = ChatIdSerializer::class)
    val chatId: ChatId
): BotCommandScope() {

    /** Scope type, must be *chat* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "chat"
}