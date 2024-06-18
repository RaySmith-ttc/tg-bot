package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents reaction changes on a message with anonymous reactions. */
@Serializable
data class MessageReactionCountUpdated(

    /** The chat containing the message */
    val chat: Chat,

    /** Unique message identifier inside the chat */
    val messageId: Int,

    /** Date of the change in Unix time */
    val date: Int,

    /** List of reactions that are present on the message */
    val reactions: List<ReactionCount>
) : ChatIdHolder {
    override fun getChatId(): ChatId {
        return chat.id
    }
}
