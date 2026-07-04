package ru.raysmith.tgbot.model.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Result of the query
 *
 * - [APPROVE] - Allow the user to join the chat
 * - [DECLINE] - Disallow the user to join the chat
 * - [QUEUE] - Leave the decision to other administrators
 * */
@Serializable
enum class ChatJoinRequestQuery {

    /** Allow the user to join the chat */
    @SerialName("approve") APPROVE,

    /** Disallow the user to join the chat */
    @SerialName("decline") DECLINE,

    /** Leave the decision to other administrators */
    @SerialName("queue") QUEUE,
}
