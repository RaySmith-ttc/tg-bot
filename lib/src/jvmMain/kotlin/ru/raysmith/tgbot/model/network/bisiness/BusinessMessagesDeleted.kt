package ru.raysmith.tgbot.model.network.bisiness

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.chat.Chat

/** This object is received when messages are deleted from a connected business account. */
@Serializable
data class BusinessMessagesDeleted(

    /** Unique identifier of the business connection */
    @SerialName("business_connection_id") val businessConnectionId: String,

    /**
     * Information about a chat in the business account. The bot may not have access to the chat or
     * the corresponding user.
     * */
    @SerialName("chat") val chat: Chat,

    /** The list of identifiers of deleted messages in the chat of the business account */
    @SerialName("message_ids") val messageIds: List<Int>,

)
