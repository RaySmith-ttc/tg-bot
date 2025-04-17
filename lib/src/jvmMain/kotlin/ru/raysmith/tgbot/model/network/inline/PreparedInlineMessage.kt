package ru.raysmith.tgbot.model.network.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message to be sent by a user of a Mini App.
 *
 * @property id Unique identifier of the prepared message
 * @property expirationDate Expiration date of the prepared message, in Unix time. Expired prepared messages can no
 *  longer be used
 * */
@Serializable
data class PreparedInlineMessage(
    @SerialName("id") val id: String,
    @SerialName("expiration_date") val expirationDate: Int
)