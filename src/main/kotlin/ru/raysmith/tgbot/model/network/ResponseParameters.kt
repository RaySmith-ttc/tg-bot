package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Contains information about why a request was unsuccessful. */
@Serializable
data class ResponseParameters(

    /** The group has been migrated to a supergroup with the specified identifier. */
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long?,

    /** In case of exceeding flood control, the number of seconds left to wait before the request can be repeated*/
    @SerialName("retry_after") val retryAfter: Long?
)