package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestUsers

/**
 * This object contains information about the user whose identifier was shared with the bot using a
 * [KeyboardButtonRequestUsers] button.
 * */
@Serializable
data class UsersShared(

    /** Identifier of the request */
    @SerialName("request_id") val requestId: Int,

    /**
     * Identifiers of the shared users. The bot may not have access to the users and could be unable to use these
     * identifiers, unless the users are already known to the bot by some other means.
     * */
    @SerialName("user_ids") val userIds: List<ChatId.ID>,
)

