package ru.raysmith.tgbot.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User

/** This object describes the access settings of a bot. */
@Serializable
data class BotAccessSettings(

    /** *True*, if only selected users can access the bot. The bot's owner can always access it. */
    @SerialName("is_access_restricted") val isAccessRestricted: Boolean,

    /** The list of other users who have access to the bot if the access is restricted */
    @SerialName("added_users") val addedUsers: List<User>? = null,
)
