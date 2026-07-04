package ru.raysmith.tgbot.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.network.API

/**
 * This object contains information about the creation, token update, or owner update of a bot that is managed by the
 * current bot.
 * */
@Serializable
data class ManagedBotUpdated(

    /** User that created the bot */
    @SerialName("user") val user: User,

    /**
     * Information about the bot. Token of the bot can be fetched using the method
     * [getManagedBotToken][API.getManagedBotToken].
     * */
    @SerialName("bot") val bot: User,
)
