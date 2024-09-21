package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.network.serializer.ChatIdSerializer

/** Represents the [scope][BotCommandScope] of bot commands, covering a specific member of a group or supergroup chat. */
@Serializable
data class BotCommandScopeChatMember(

    /** Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`) */
    @SerialName("chat_id")
    @Serializable(with = ChatIdSerializer::class)
    val chatId: ChatId,

    /** Unique identifier of the target user */
    @SerialName("user_id")
    val userId: ChatId.ID
): BotCommandScope() {

    /** Scope type, must be *chat_member* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "chat_member"
}