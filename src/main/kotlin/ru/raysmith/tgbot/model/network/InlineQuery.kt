package ru.raysmith.tgbot.model.network

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.model.network.chat.ChatType
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

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
    @SerialName("chat_type") val chat_type: ChatType,

    /** Sender location, only for bots that request user location */
    @SerialName("location") val location: Location? = null,
) : ChatIdHolder {
    override fun getChatId() = from.id
}

@Polymorphic
@Serializable
sealed class InlineQueryResult {
    abstract val type: String
}

@Serializable
data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    @SerialName("hide_url") val hideUrl: Boolean? = null,
    val description: String? = null,
    @SerialName("thumb_url") val thumbUrl: String? = null,
    @SerialName("thumb_width") val thumbWidth: Int? = null,
    @SerialName("thumb_height") val thumbHeight: Int? = null
) : InlineQueryResult() {

    @EncodeDefault
    override val type: String = "article"
}

@Polymorphic
@Serializable
sealed class InputMessageContent

@Serializable
data class InputTextMessageContent(
    @SerialName("message_text") val messageText: String,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("entities") val entities: List<MessageEntity>? = null,
    @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null
) : InputMessageContent()