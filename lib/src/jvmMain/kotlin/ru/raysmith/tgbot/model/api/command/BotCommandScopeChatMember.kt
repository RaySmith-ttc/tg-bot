package ru.raysmith.tgbot.model.api.command

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId

/**
 * Represents the [scope][BotCommandScope] of bot commands, covering a specific member of a group or supergroup chat.
 * */
@Serializable
data class BotCommandScopeChatMember(

    /**
     * Unique identifier for the target chat or username of the target supergroup. Channel direct messages chats and
     * channel chats aren't supported.
     * */
    @SerialName("chat_id")
    val chatId: ChatId,

    /** Unique identifier of the target user */
    @SerialName("user_id")
    val userId: ChatId.ID,
): BotCommandScope {

    /** Scope type, must be *chat_member* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "chat_member"
}
