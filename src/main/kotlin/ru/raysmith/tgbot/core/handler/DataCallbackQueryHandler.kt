package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery

data class DataCallbackQueryHandler(override val query: CallbackQuery, val data: String) : BaseCallbackHandler(query)
