package ru.raysmith.tgbot.model.api.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.model.api.chat.Chat

/** This object represents an answer of a user in a non-anonymous poll. */
@Serializable
data class PollAnswer(

    /** Unique poll identifier */
    @SerialName("poll_id") val pollId: String,

    /** The chat that changed the answer to the poll, if the voter is anonymous */
    @SerialName("voter_chat") val voterChat: Chat? = null,

    /** The user, who changed the answer to the poll */
    @SerialName("user") val user: User? = null,

    /** 0-based identifiers of chosen answer options. May be empty if the vote was retracted. */
    @SerialName("option_ids") val optionIds: List<Int>,

    /** Persistent identifiers of the chosen answer options. May be empty if the vote was retracted. */
    @SerialName("option_persistent_ids") val optionPersistentIds: List<String>,
) {
    fun isRetracted() = optionIds.isEmpty() && optionPersistentIds.isEmpty()
}
