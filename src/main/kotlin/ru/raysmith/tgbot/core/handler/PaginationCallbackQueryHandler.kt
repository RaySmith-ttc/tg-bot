package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery

data class PaginationCallbackQueryHandler(override val query: CallbackQuery, val page: Long) : BaseCallbackHandler(query)