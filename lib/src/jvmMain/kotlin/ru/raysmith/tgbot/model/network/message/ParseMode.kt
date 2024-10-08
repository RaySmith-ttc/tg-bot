package ru.raysmith.tgbot.model.network.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * # Formatting options
 * The Bot API supports basic formatting for messages. You can use bold, italic, underlined, strikethrough, and spoiler
 * text, block quotations as well as inline links and pre-formatted code in your bots' messages.
 * Telegram clients will render them accordingly. You can use either markdown-style or HTML-style formatting.
 *
 * Note that Telegram clients will display an **alert** to the user before opening an inline link
 * ('Open this link?' together with the full URL).
 *
 * Message entities can be nested, providing following restrictions are met:
 * - If two entities has common characters then one of them is fully contained inside another.
 * - *bold*, *italic*, *underline* and *strikethrough* entities can contain and to be contained in any other entities,
 * except pre and code.
 * - *blockquote* and *expandable_blockquote* entities can't be nested.
 * - All other entities can't contain each other.
 *
 * Links `tg://user?id=<user_id>` can be used to mention a user by their identifier without using a username.
 * Please note:
 * - These links will work **only** if they are used inside an inline link. For example, they will not work, when used
 * in an inline keyboard button or in a message text.
 * - These mentions are only guaranteed to work if the user has contacted the bot in the past, has sent a callback
 * query to the bot via inline button or is a member in the group where he was mentioned.
 *
 * You can find the list of programming and markup languages for which syntax highlighting is supported at
 * [libprisma](https://github.com/TelegramMessenger/libprisma#supported-languages).
 *
 * ## MarkdownV2 style
 * To use this mode, pass *MarkdownV2* in the *parse_mode* field. Use the following syntax in your message:
 * ```
 * *bold \*text*
 * _italic \*text_
 * __underline__
 * ~strikethrough~
 * *bold _italic bold ~italic bold strikethrough~ __underline italic bold___ bold*
 * [inline URL](http://www.example.com/)
 * [inline mention of a user](tg://user?id=123456789)
 * `inline fixed-width code`
 *
 * ````
 * ````
 *
 * ```
 * pre-formatted fixed-width code block
 * ```
 *
 * ````
 * ````
 *
 * ```python
 * pre-formatted fixed-width code block written in the Python programming language
 * ```
 * ````
 * ```
 * >Block quotation started
 * >Block quotation continued
 * >Block quotation continued
 * >Block quotation continued
 * >The last line of the block quotation
 * **>The expandable block quotation started right after the previous block quotation
 * >It is separated from the previous block quotation by an empty bold entity
 * >Expandable block quotation continued
 * >Hidden by default part of the expandable block quotation started
 * >Expandable block quotation continued
 * >The last line of the expandable block quotation with the expandability mark||
 * ```
 *
 * Please note:
 * - Any character with code between 1 and 126 inclusively can be escaped anywhere with a preceding '\' character,
 * in which case it is treated as an ordinary character and not a part of the markup.
 * - Inside `pre` and `code` entities, all '&#96;' and '\' characters must be escaped with a preceding '\' character.
 * - Inside `(...)` part of inline link definition, all ')' and '\' must be escaped with a preceding '\' character.
 * - In all other places characters '_', '*', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}',
 * '.', '!' must be escaped with the preceding character '\'.
 * - In case of ambiguity between `italic` and `underline` entities `__` is always greadily treated from left to right as
 * beginning or end of `underline` entity, so instead of `___italic underline___` use `___italic underline_**__`,
 * adding an empty bold entity as a separator.
 *
 * ## HTML style
 * To use this mode, pass *HTML* in the *parse_mode* field. The following tags are currently supported:
 * ```
 * <b>bold</b>, <strong>bold</strong>
 * <i>italic</i>, <em>italic</em>
 * <u>underline</u>, <ins>underline</ins>
 * <s>strikethrough</s>, <strike>strikethrough</strike>, <del>strikethrough</del>
 * <b>bold <i>italic bold <s>italic bold strikethrough</s> <u>underline italic bold</u></i> bold</b>
 * <a href="http://www.example.com/">inline URL</a>
 * <a href="tg://user?id=123456789">inline mention of a user</a>
 * <code>inline fixed-width code</code>
 * <pre>pre-formatted fixed-width code block</pre>
 * <pre><code class="language-python">pre-formatted fixed-width code block written in the Python programming language</code></pre>
 * ```
 *
 * Please note:
 * - Only the tags mentioned above are currently supported.
 * - All `<`, `>` and `&` symbols that are not a part of a tag or an HTML entity must be replaced with the corresponding
 * HTML entities (`<` with `&lt;`, `>` with `&gt;` and `&` with `&amp;`).
 * - All numerical HTML entities are supported.
 * - The API currently supports only the following named HTML entities: `&lt;`, `&gt;`, `&amp;` and `&quot;`.
 * - Use nested `pre` and `code` tags, to define programming language for `pre` entity.
 * - Programming language can't be specified for standalone `code` tags.
 *
 * ## Markdown style
 * This is a legacy mode, retained for backward compatibility. To use this mode, pass *Markdown* in the
 * *parse_mode* field. Use the following syntax in your message:
 *
 * ```
 * *bold text*
 * _italic text_
 * [inline URL](http://www.example.com/)
 * [inline mention of a user](tg://user?id=123456789)
 * `inline fixed-width code`
 *
 *
 * ````
 * ````
 *
 * ```
 * pre-formatted fixed-width code block
 * ```
 *
 * ````
 * ````
 *
 * ```python
 * pre-formatted fixed-width code block written in the Python programming language
 * ```
 * ````
 * ```
 *
 * Please note:
 * - Entities must not be nested, use parse mode MarkdownV2 instead.
 * - There is no way to specify “underline”, “strikethrough”, “spoiler”, “blockquote”, “expandable_blockquote”
 * and “custom_emoji” entities, use parse mode MarkdownV2 instead.
 * - To escape characters '_', '*', '`', '[' outside of an entity, prepend the characters '\' before them.
 * - Escaping inside entities is not allowed, so entity must be closed first and reopened again: use `_snake_\__case_`
 * for italic `snake_case` and `*2*\**2=4*` for bold `2*2=4`.
 * */
@Serializable
enum class ParseMode {
    @SerialName("HTML") HTML,

    @Deprecated(
        "This is a legacy mode, retained for backward compatibility",
        ReplaceWith("MARKDOWNV2", "ru.raysmith.tgbot.model.network.message.ParseMode")
    )
    @SerialName("Markdown")
    MARKDOWN,

    @SerialName("MarkdownV2")
    MARKDOWNV2
}