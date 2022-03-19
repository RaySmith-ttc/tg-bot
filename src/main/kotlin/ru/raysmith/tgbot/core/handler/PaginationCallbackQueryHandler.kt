package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramService

data class PaginationCallbackQueryHandler(
    override val query: CallbackQuery,
    val page: Long,
    override val service: TelegramService
) : BaseCallbackHandler(query, service)