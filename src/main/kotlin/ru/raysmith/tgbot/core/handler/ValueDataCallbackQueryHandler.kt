package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramService

data class ValueDataCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    override val service: TelegramService
) : BaseCallbackHandler(query, service)
