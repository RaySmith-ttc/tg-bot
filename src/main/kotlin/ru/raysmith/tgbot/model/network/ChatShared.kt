package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestChat

/**
 * This object contains information about the chat whose identifier was shared with the bot using a
 * [KeyboardButtonRequestChat] button.
 * */
@Serializable
data class ChatShared(

    /** Identifier of the request */
    @SerialName("request_id") val requestId: Int,

    /**
     * Identifier of the shared user.
     * The bot may not have access to the user and could be unable to use this identifier, unless the user
     * is already known to the bot by some other means.
     * */
    @SerialName("chat_id") val chatId: ChatId.ID,
)