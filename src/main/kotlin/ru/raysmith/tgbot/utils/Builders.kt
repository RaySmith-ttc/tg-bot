package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.message.ParseMode

suspend inline fun buildHTMLString(
    type: MessageTextType = MessageTextType.TEXT, botConfig: BotConfig = BotConfig.default,
    crossinline block: suspend MessageText.() -> Unit
): String {
    return MessageText(type, botConfig).apply { block() }.format(ParseMode.HTML)
}

suspend inline fun buildMarkdownV2String(
    type: MessageTextType = MessageTextType.TEXT, botConfig: BotConfig = BotConfig.default,
    crossinline block: suspend MessageText.() -> Unit
): String {
    return MessageText(type, botConfig).apply { block() }.format(ParseMode.MARKDOWNV2)
}

@Deprecated(
    "This is a legacy mode, retained for backward compatibility, use buildMarkdownV2MessageText instead",
    ReplaceWith("buildMarkdownV2String {block()}")
)
suspend inline fun buildMarkdownString(
    type: MessageTextType = MessageTextType.TEXT, botConfig: BotConfig = BotConfig.default,
    crossinline block: suspend MessageText.() -> Unit
): String {
    return MessageText(type, botConfig).apply { block() }.format(@Suppress("DEPRECATION") ParseMode.MARKDOWN)
}