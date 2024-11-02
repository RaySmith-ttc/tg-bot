package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Type of the entity. */
@Serializable
enum class MessageEntityType {

    /** Example: @username */
    @SerialName("mention") MENTION,

    /** Example: #hashtag or #hashtag@chatusername */
    @SerialName("hashtag") HASHTAG,

    /** Example: $USD or $USD@chatusername */
    @SerialName("cashtag") CASHTAG,

    /** Example: /start@jobs_bot */
    @SerialName("bot_command") BOT_COMMAND,

    /** Example: https://telegram.org */
    @SerialName("url") URL,

    /** Example: do-not-reply@telegram.org */
    @SerialName("email") EMAIL,

    /** Example: +1-212-555-0123 */
    @SerialName("phone_number") PHONE_NUMBER,

    /** **Bold text** */
    @SerialName("bold") BOLD,

    /** *Italic text* */
    @SerialName("italic") ITALIC,

    /** Underlined text */
    @SerialName("underline") UNDERLINE,

    /** Strikethrough text */
    @SerialName("strikethrough") STRIKETHROUGH,

    /** Spoiler message */
    @SerialName("spoiler") SPOILER,

    /** Block quotation */
    @SerialName("blockquote") BLOCKQUOTE,

    /** Collapsed-by-default block quotation */
    @SerialName("expandable_blockquote") EXPANDABLE_BLOCKQUOTE,

    /** Example: `monowidth string` */
    @SerialName("code") CODE,

    /**
     * ```
     * Monowidth block
     * ```
     * */
    @SerialName("pre") PRE,

    /** For clickable text URLs */
    @SerialName("text_link") TEXT_LINK,

    /** For users [without usernames](https://telegram.org/blog/edit#new-mentions) */
    @SerialName("text_mention") TEXT_MENTION,

    /** For inline custom emoji stickers */
    @SerialName("custom_emoji") CUSTOM_EMOJI;
}