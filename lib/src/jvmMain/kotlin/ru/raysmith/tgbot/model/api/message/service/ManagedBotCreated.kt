package ru.raysmith.tgbot.model.api.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.user.User
import ru.raysmith.tgbot.network.API

/** This object contains information about the bot that was created to be managed by the current bot. */
@Serializable
data class ManagedBotCreated(

    /**
     * Information about the bot. The bot's token can be fetched using the method
     * [getManagedBotToken][API.getManagedBotToken].
     * */
    @SerialName("bot") val bot: User,
)
