package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId

// TODO link KeyboardButtonRequestUser
/**
 * This object contains information about the user whose identifier was shared with the bot using a
 * [KeyboardButtonRequestUser] button.
 * */
@Serializable
data class UserShared(

    /** Identifier of the request */
    @SerialName("request_id") val requestId: Int,

    /**
     * Identifier of the shared user.
     * The bot may not have access to the user and could be unable to use this identifier, unless the user
     * is already known to the bot by some other means.
     * */
    @SerialName("user_id") val userId: ChatId.ID,
)

