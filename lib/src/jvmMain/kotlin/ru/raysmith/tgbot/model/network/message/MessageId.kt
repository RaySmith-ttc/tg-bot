package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a unique message identifier. */
@Serializable
data class MessageId(

    /** Unique message identifier */
    @SerialName("message_id") val messageId: Int
)