package ru.raysmith.tgbot.model.network.chat

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.utils.toChatId

interface IChat {
    val id: ChatId.ID
    val type: ChatType
    val username: String?

    /**
     * Ban a user in the group, supergroup or channel
     *
     * @param userId Unique identifier of the target user
     * @param untilDate Date when the user will be unbanned, unix time.
     * If user is banned for more than 366 days or less than 30 seconds from the current time they are considered
     * to be banned forever. Applied for supergroups and channels only.
     * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     *
     * @see API.banChatMember
     * @throws IllegalArgumentException if the chat is not a group, supergroup or channel
     * */
    context(BotContext<*>)
    suspend fun ban(userId: ChatId.ID, untilDate: Until? = null, revokeMessages: Boolean? = null) {
        val id = when(type) {
            ChatType.GROUP -> id
            ChatType.SUPERGROUP, ChatType.CHANNEL -> username!!.toChatId()
            else -> error("Chat must be a group, supergroup or channel")
        }
        banChatMember(id, userId, untilDate, revokeMessages)
    }

    /**
     * Unban a user in the group, supergroup or channel
     *
     * @param userId Unique identifier of the target user
     * @param onlyIfBanned Do nothing if the user is not banned
     *
     * @see API.unbanChatMember
     * @throws IllegalArgumentException if the chat is not a group, supergroup or channel
     * */
    context(BotContext<*>)
    suspend fun unban(userId: ChatId.ID, onlyIfBanned: Boolean? = null) {
        val id = when(type) {
            ChatType.GROUP -> id
            ChatType.SUPERGROUP, ChatType.CHANNEL -> username!!.toChatId()
            else -> error("Chat must be a group, supergroup or channel")
        }
        unbanChatMember(id, userId, onlyIfBanned)
    }
}