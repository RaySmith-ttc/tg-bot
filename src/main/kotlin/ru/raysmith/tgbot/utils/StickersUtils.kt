package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler

/** Returns sticker name with base [name] appended with `_by_<bot_username>` */
fun <T : EventHandler> BotContext<T>.stickerSetName(name: String) = "${name}_by_${bot.me.username}"

/** Returns share link of a sticker set with [name] */
fun <T : EventHandler> BotContext<T>.stickerSetShareLink(name: String) = "https://t.me/addstickers/${name}_by_${bot.me.username}"

