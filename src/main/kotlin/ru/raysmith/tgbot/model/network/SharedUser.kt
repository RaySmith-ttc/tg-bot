package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestUsers
import ru.raysmith.tgbot.model.network.media.PhotoSize

/**
 * This object contains information about a user that was shared with the bot using a
 * [KeyboardButtonRequestUsers] button.
 * */
@Serializable
data class SharedUser(

    /**
     * Identifier of the shared user. The bot may not have access to the user and could be unable to use this
     * identifier, unless the user is already known to the bot by some other means.
     * */
    @SerialName("user_id") val userId: ChatId.ID,

    /** First name of the user, if the name was requested by the bot */
    @SerialName("first_name") val firstName: String? = null,

    /** Last name of the user, if the name was requested by the bot */
    @SerialName("last_name") val lastName: String? = null,

    /** Username of the user, if the username was requested by the bot */
    @SerialName("username") val username: String? = null,

    /** Available sizes of the chat photo, if the photo was requested by the bot */
    @SerialName("photo") val photo: List<PhotoSize>? = null,
)