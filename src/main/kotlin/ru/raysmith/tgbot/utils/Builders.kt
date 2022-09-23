package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.message.ParseMode

inline fun buildHTMLString(
    type: MessageTextType = MessageTextType.TEXT,
    block: MessageText.() -> Unit
): String {
    return MessageText(type).apply(block).format(ParseMode.HTML)
}

inline fun buildMarkdownV2String(
    type: MessageTextType = MessageTextType.TEXT,
    block: MessageText.() -> Unit
): String {
    return MessageText(type).apply(block).format(ParseMode.MARKDOWNV2)
}

@Deprecated(
    "This is a legacy mode, retained for backward compatibility, use buildMarkdownV2MessageText instead",
    ReplaceWith("buildMarkdownV2String {block()}")
)
inline fun buildMarkdownString(
    type: MessageTextType = MessageTextType.TEXT,
    block: MessageText.() -> Unit
): String {
    return MessageText(type).apply(block).format(@Suppress("DEPRECATION") ParseMode.MARKDOWN)
}