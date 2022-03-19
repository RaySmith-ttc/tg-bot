package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.model.bot.MessageText
import ru.raysmith.tgbot.model.network.message.ParseMode

inline fun buildHTMLString(safeLength: Boolean = true, block: MessageText.() -> Unit): String {
    return MessageText(false).apply(block).format(ParseMode.HTML, safeLength)
}

inline fun buildMarkdownV2String(safeLength: Boolean = true, block: MessageText.() -> Unit): String {
    return MessageText(false).apply(block).format(ParseMode.MARKDOWNV2, safeLength)
}

@Deprecated(
    "This is a legacy mode, retained for backward compatibility, use buildMarkdownV2MessageText instead",
    ReplaceWith("buildMarkdownV2String {block()}")
)
inline fun buildMarkdownString(safeLength: Boolean = true, block: MessageText.() -> Unit): String
    = MessageText(false).apply(block).format(ParseMode.MARKDOWN, safeLength)