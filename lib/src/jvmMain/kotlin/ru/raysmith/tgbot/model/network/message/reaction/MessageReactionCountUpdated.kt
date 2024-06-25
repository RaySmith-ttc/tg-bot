package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents reaction changes on a message with anonymous reactions. */
@Serializable
data class MessageReactionCountUpdated(

    /** The chat containing the message */
    @SerialName("chat") val chat: Chat,

    /** Unique message identifier inside the chat */
    @SerialName("message_id") val messageId: Int,

    /** Date of the change in Unix time */
    @SerialName("date") val date: Int,

    /** List of reactions that are present on the message */
    @SerialName("reactions") val reactions: List<ReactionCount>
) : ChatIdHolder {
    override fun getChatId(): ChatId {
        return chat.id
    }
}
