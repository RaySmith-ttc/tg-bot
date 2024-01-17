package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.network.API

/**
 * Returns sticker name with base [name] appended with `_by_<bot_username>`
 *
 * @param bot current bot for getting [Bot.me]. If it is null then [getMe][API.getMe] was called
 * */
suspend fun <T : EventHandler> BotContext<T>.stickerSetName(name: String, bot: Bot? = null) =
    "${name}_by_${bot?.me?.username ?: getMe().username}"