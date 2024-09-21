package ru.raysmith.tgbot.model.network.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.network.Location
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.chat.ChatType

/**
 * This object represents an incoming inline query.
 * When the user sends an empty query, your bot could return some default or trending results.
 * */
@Serializable
data class InlineQuery(

    /** Unique identifier for this query */
    @SerialName("id") val id: String,

    /** Sender */
    @SerialName("from") val from: User,

    /** Text of the query (up to 256 characters) */
    @SerialName("query") val query: String,

    /** Offset of the results to be returned, can be controlled by the bot */
    @SerialName("offset") val offset: String,

    /** Type of the chat, from which the inline query was sent. Can be either “sender” for a private chat with the
     * inline query sender, “private”, “group”, “supergroup”, or “channel”. The chat type should be always known for
     * requests sent from official clients and most third-party clients, unless the request was sent from a secret chat
     * */
    @SerialName("chat_type") val chatType: ChatType,

    /** Sender location, only for bots that request user location */
    @SerialName("location") val location: Location? = null,
) : ChatIdHolder {
    override fun getChatId() = from.id
    override fun getChatIdOrThrow() = from.id
}