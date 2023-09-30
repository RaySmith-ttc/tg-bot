package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.model.network.CallbackQuery

data class PaginationCallbackQueryHandler(
    override val query: CallbackQuery,
    val page: Long,
    override val client: HttpClient
) : BaseCallbackHandler(query, client)