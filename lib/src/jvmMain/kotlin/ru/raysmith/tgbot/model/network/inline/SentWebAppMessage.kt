package ru.raysmith.tgbot.model.network.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a [Web App](https://core.telegram.org/bots/webapps) on behalf of a user.
 * */
@Serializable
data class SentWebAppMessage(
    
    /**
     * Identifier of the sent inline message. Available only if there is an
     * [inline keyboard](https://core.telegram.org/bots/api#inlinekeyboardmarkup) attached to the message.
     * */
    @SerialName("inline_message_id") val inlineMessageId: String? = null
)