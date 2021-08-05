package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery

data class ValueDataCallbackQueryHandler(override val query: CallbackQuery, val data: String, val value: String) : BaseCallbackHandler(query)