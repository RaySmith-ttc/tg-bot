package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestUsers

/**
 * This object contains information about the user whose identifier was shared with the bot using a
 * [KeyboardButtonRequestUsers] button.
 * */
@Serializable
data class UsersShared(

    /** Identifier of the request */
    @SerialName("request_id") val requestId: Int,

    /** Information about users shared with the bot. */
    @SerialName("users") val users: List<SharedUser>,
)

