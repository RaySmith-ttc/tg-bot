package ru.raysmith.tgbot.model.network.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the [scope][BotCommandScope] of bot commands, covering a specific member of a group or supergroup chat. */
@Serializable
data class BotCommandScopeChatMember(

    /** Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`) */
    @SerialName("chat_id")
    val chatId: String,

    /** Unique identifier of the target user */
    @SerialName("user_id")
    val userId: Long
): BotCommandScope() {

    /** Scope type, must be *chat_member* */
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val type: String = "chat_member"
}