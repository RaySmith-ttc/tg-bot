package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.network.API

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 *
 * @param type See [MessageEntityType]
 * @param offset Offset in UTF-16 code units to the start of the entity
 * @param length Length of the entity in UTF-16 code units
 * @param url For “text_link” only, url that will be opened after user taps on the text
 * @param user For “text_mention” only, the mentioned user
 * @param language For “pre” only, the programming language of the entity text
 *
 * @see <a href="https://core.telegram.org/bots/api#messageentity">api#messageentity</a>
 * */

@Serializable
data class MessageEntity(

    /** See [MessageEntityType] */
    @SerialName("type") val type: MessageEntityType,

    /** Offset in UTF-16 code units to the start of the entity */
    @SerialName("offset") val offset: Int,

    /** Length of the entity in UTF-16 code units */
    @SerialName("length") val length: Int,

    /** For “text_link” only, url that will be opened after user taps on the text */
    @SerialName("url") val url: String? = null,

    /** For “text_mention” only, the mentioned user */
    @SerialName("user") val user: User? = null,

    /** For “pre” only, the programming language of the entity text */
    @SerialName("language") val language: String? = null,

    /**
     * For "custom_emoji" only, unique identifier of the custom emoji.
     * Use [getCustomEmojiStickers][API.getCustomEmojiStickers] to get full information about the sticker
     * */
    @SerialName("custom_emoji_id") val customEmojiId: String? = null,
) {

    @Suppress("Deprecation")
    fun formatString(string: String, parseMode: ParseMode): String {
        fun getForParseMode(html: String, md: String, md2: String): String {
            return when(parseMode) {
                ParseMode.HTML -> html
                ParseMode.MARKDOWN -> md
                ParseMode.MARKDOWNV2 -> md2
            }
        }

        fun String.linkEsc() = this.replace(")", "\\)").replace("\\", "\\\\")

        return when(type) {
            MessageEntityType.BOLD -> getForParseMode("<b>$string</b>", "*$string*", "*$string*")
            MessageEntityType.ITALIC -> getForParseMode("<i>$string</i>", "_${string}_", "_${string}_")
            MessageEntityType.UNDERLINE -> getForParseMode("<u>$string</u>", string, "__${string}__")
            MessageEntityType.STRIKETHROUGH -> getForParseMode("<s>$string</s>", string, "~${string}~")
            MessageEntityType.SPOILER -> getForParseMode(
                "<span class=\"tg-spoiler\">$string</span>", string, "||$string||"
            )
            MessageEntityType.URL ->  getForParseMode(
                "<a href=\"${string}\">$string</a>", "[$string](${string.linkEsc()})", "[$string](${string.linkEsc()})"
            )
            MessageEntityType.TEXT_LINK -> getForParseMode(
                "<a href=\"${url}\">$string</a>", "[$string](${url?.linkEsc()})", "[$string](${url?.linkEsc()})"
            )
            MessageEntityType.MENTION -> getForParseMode(
                "<a href=\"mention:$string\">$string</a>", string, "[$string](mention:$string)"
            )
            MessageEntityType.TEXT_MENTION -> {
                getForParseMode(
                    "<a href=\"tg://user?id=${user?.id?.value}\">$string</a>",
                    string,
                    "[$string](tg://user?id=${user?.id?.value})"
                )
            }
            MessageEntityType.CODE -> getForParseMode("<code>$string</code>", "`$string`", "`$string`")
            MessageEntityType.PRE -> getForParseMode(
                "<pre>${language?.let { "<code class=\"language-$it\">" } ?: ""}$string${language?.let { "</code>" } ?: ""}</pre>",
                "```${language ?: ""}\n$string\n```",
                "```${language ?: ""}\n$string\n```",
            )
            MessageEntityType.CUSTOM_EMOJI -> getForParseMode(
                "<tg-emoji${customEmojiId?.let { " emoji-id=\"$it\"" } ?: ""}>$string</tg-emoji>",
                "![$string]${customEmojiId?.let { "(tg://emoji?id=$it)" } ?: ""}",
                string
            )
            else -> string
        }
    }
}
