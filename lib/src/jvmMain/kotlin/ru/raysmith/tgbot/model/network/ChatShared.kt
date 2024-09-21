package ru.raysmith.tgbot.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButtonRequestChat
import ru.raysmith.tgbot.model.network.media.PhotoSize

/**
 * This object contains information about a chat that was shared with the bot using a
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

    /** Title of the chat, if the title was requested by the bot. */
    @SerialName("title") val title: String? = null,

    /** Username of the chat, if the username was requested by the bot and available. */
    @SerialName("username") val username: String? = null,

    /** Available sizes of the chat photo, if the photo was requested by the bot */
    @SerialName("photo") val photo: List<PhotoSize>? = null,
)