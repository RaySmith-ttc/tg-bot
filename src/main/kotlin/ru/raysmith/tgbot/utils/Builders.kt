package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.model.bot.MessageText
import ru.raysmith.tgbot.model.bot.MessageTextType
import ru.raysmith.tgbot.model.network.message.ParseMode

inline fun buildHTMLString(
    type: MessageTextType = MessageTextType.TEXT,
    printNulls: Boolean = false,
    safeLength: Boolean = true,
    block: MessageText.() -> Unit
): String {
    return MessageText(printNulls, type).apply(block).format(ParseMode.HTML, safeLength)
}

inline fun buildMarkdownV2String(
    type: MessageTextType = MessageTextType.TEXT,
    printNulls: Boolean = false,
    safeLength: Boolean = true,
    block: MessageText.() -> Unit
): String {
    return MessageText(printNulls, type).apply(block).format(ParseMode.MARKDOWNV2, safeLength)
}

@Deprecated(
    "This is a legacy mode, retained for backward compatibility, use buildMarkdownV2MessageText instead",
    ReplaceWith("buildMarkdownV2String {block()}")
)
inline fun buildMarkdownString(
    type: MessageTextType = MessageTextType.TEXT,
    printNulls: Boolean = false,
    safeLength: Boolean = true,
    block: MessageText.() -> Unit
): String {
    return MessageText(printNulls, type).apply(block).format(ParseMode.MARKDOWN, safeLength)
}