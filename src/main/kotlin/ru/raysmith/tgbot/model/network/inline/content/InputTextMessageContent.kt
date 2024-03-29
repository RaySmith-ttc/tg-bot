package ru.raysmith.tgbot.model.network.inline.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode

/** Represents the [content][InputMessageContent] of a text message to be sent as the result of an inline query. */
@Serializable
data class InputTextMessageContent(

    /** Text of the message to be sent, 1-4096 characters */
    @SerialName("message_text") val messageText: String,

    /** Mode for parsing entities in the message text. See [formatting options][ParseMode] for more details. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,

    /** List of special entities that appear in message text, which can be specified instead of [parseMode] */
    @SerialName("entities") val entities: List<MessageEntity>? = null,

    /** Disables link previews for links in the sent message */
    @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null
) : InputMessageContent()