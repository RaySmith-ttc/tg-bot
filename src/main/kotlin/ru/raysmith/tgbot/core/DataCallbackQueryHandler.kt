package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.CallbackQuery

data class DataCallbackQueryHandler(override val query: CallbackQuery, val data: String) : BaseCallbackHandler(query)