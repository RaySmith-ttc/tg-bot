package ru.raysmith.tgbot.model.api.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Describes an inline message sent by a guest bot. */
@Serializable
data class SentGuestMessage(

    /** Identifier of the sent inline message */
    @SerialName("inline_message_id") val inlineMessageId: String,
)
