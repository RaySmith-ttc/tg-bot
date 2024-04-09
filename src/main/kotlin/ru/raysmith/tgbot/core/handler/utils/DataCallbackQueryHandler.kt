package ru.raysmith.tgbot.core.handler.utils

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.base.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery

data class DataCallbackQueryHandler(
    override val query: CallbackQuery,
    val data: String,
    override val bot: Bot
) : BaseCallbackHandler(query, bot.client), BotHolder {
    override val botConfig: BotConfig = bot.botConfig
}
