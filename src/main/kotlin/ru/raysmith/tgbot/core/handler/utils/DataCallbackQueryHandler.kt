package ru.raysmith.tgbot.core.handler.utils

import io.ktor.client.*
import ru.raysmith.tgbot.core.handler.base.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery

data class DataCallbackQueryHandler(
    override val query: CallbackQuery,
    val data: String,
    override val client: HttpClient
) : BaseCallbackHandler(query, client)
