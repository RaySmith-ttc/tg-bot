package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 *
 * Note: It is necessary to enable inline feedback via @Botfather in order to receive these objects in updates.
 * */
@Serializable
data class ChosenInlineResult(

    /** The unique identifier for the result that was chosen */
    @SerialName("result_id") val resultId: String,

    /** The user that chose the result */
    @SerialName("from") val from: User,

    /** Sender location, only for bots that require user location */
    @SerialName("location") val location: Location? = null,

    /**
     * Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     * Will be also received in callback queries and can be used to edit the message.
     * */
    @SerialName("inline_message_id") val inlineMessageId: String? = null,

    /** The query that was used to obtain the result */
    @SerialName("query") val query: String,
) : ChatIdHolder {
    override fun getChatId() = from.id
    override fun getChatIdOrThrow() = from.id
}