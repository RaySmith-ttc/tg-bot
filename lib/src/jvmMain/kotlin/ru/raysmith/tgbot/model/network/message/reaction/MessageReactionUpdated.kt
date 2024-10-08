package ru.raysmith.tgbot.model.network.message.reaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents a change of a reaction on a message performed by a user. */
@Serializable
data class MessageReactionUpdated(

    /** The chat containing the message the user reacted to */
    @SerialName("chat") val chat: Chat,

    /** Unique identifier of the message inside the chat */
    @SerialName("message_id") val messageId: Int,

    /** The user that changed the reaction, if the user isn't anonymous */
    @SerialName("user") val user: User? = null,

    /** The chat on behalf of which the reaction was changed, if the user is anonymous */
    @SerialName("actor_chat") val actorChat: Chat? = null,

    /** Date of the change in Unix time */
    @SerialName("date") val date: Int,

    /** Previous list of reaction types that were set by the user */
    @SerialName("old_reaction") val oldReaction: List<ReactionType>,

    /** New list of reaction types that have been set by the user */
    @SerialName("new_reaction") val newReaction: List<ReactionType>

) : ChatIdHolder {
    override fun getChatId(): ChatId {
        return chat.id
    }
}
