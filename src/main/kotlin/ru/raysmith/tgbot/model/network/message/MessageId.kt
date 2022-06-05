package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/** This object represents a unique message identifier. */
data class MessageId(

    /** Unique message identifier */
    @SerialName("message_id") val messageId: Int
)