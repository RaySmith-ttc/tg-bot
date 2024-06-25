package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object represents an answer of a user in a non-anonymous poll. */
@Serializable
data class PollAnswer(

    /** Unique poll identifier */
    @SerialName("poll_id") val pollId: String,

    /** The chat that changed the answer to the poll, if the voter is anonymous */
    @SerialName("voter_chat") val voterChat: Chat? = null,

    /** The user, who changed the answer to the poll */
    @SerialName("user") val user: User? = null,

    /** 0-based identifiers of answer options, chosen by the user. May be empty if the user retracted their vote. */
    @SerialName("option_ids") val optionIds: List<Int>,
) {
    fun isRetracted() = optionIds.isEmpty()
}