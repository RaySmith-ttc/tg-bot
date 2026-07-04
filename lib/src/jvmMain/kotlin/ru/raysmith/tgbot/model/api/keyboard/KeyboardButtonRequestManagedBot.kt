package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.updates.Update

/**
 * This object defines the parameters for the creation of a managed bot. Information about the created bot will be
 * shared with the bot using the update [managedBot][Update.managedBot] and a [Message] with the field
 * [managedBotCreated][Message.managedBotCreated].
 * */
@Serializable
data class KeyboardButtonRequestManagedBot(

    /** Signed identifier of the request. Must be unique within the message. */
    @SerialName("request_id") val requestId: Int,

    /** Suggested name for the bot */
    @SerialName("suggested_name") val suggestedName: String? = null,

    /** Suggested username for the bot */
    @SerialName("suggested_username") val suggestedUsername: String? = null,
)
