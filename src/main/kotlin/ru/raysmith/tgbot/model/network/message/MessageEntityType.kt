package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/**
 * Type of the entity. Can be “mention” (@username), “hashtag” (#hashtag), “cashtag” ($USD),
 * “bot_command” (/start@jobs_bot), “url” (https://telegram.org), “email” (do-not-reply@telegram.org),
 * “phone_number” (+1-212-555-0123), “bold” (bold text), “italic” (italic text), “underline” (underlined text),
 * “strikethrough” (strikethrough text), “code” (monowidth string), “pre” (monowidth block),
 * “text_link” (for clickable text URLs), “text_mention” (for users without usernames)
 *
 * @see <a href="https://telegram.org/blog/edit#new-mentions">New Mentions</a>
 *
 * */
enum class MessageEntityType {
    @SerialName("mention") MENTION,
    @SerialName("hashtag") HASHTAG,
    @SerialName("cashtag") CASHTAG,
    @SerialName("bot_command") BOT_COMMAND,
    @SerialName("url") URL,
    @SerialName("email") EMAIL,
    @SerialName("phone_number") PHONE_NUMBER,
    @SerialName("bold") BOLD,
    @SerialName("italic") ITALIC,
    @SerialName("underline") UNDERLINE,
    @SerialName("strikethrough") STRIKETHROUGH,
    @SerialName("code") CODE,
    @SerialName("pre") PRE,
    @SerialName("text_link") TEXT_LINK,
    @SerialName("text_mention") TEXT_MENTION
}