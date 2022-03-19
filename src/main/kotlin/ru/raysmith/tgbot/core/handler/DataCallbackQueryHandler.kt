package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramService

data class DataCallbackQueryHandler(
    override val query: CallbackQuery,
    val data: String,
    override val service: TelegramService
) : BaseCallbackHandler(query, service)
