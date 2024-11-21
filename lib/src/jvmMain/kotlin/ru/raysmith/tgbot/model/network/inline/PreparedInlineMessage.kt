package ru.raysmith.tgbot.model.network.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message to be sent by a user of a Mini App.
 */
@Serializable
data class PreparedInlineMessage(

    /** Unique identifier of the prepared message.*/
    @SerialName("id") val id: String,

    /** Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used. */
    @SerialName("expiration_date") val expirationDate: Int
)