package ru.raysmith.tgbot.core.handler.utils

import io.ktor.client.*
import ru.raysmith.tgbot.core.handler.base.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

data class ValueDataCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    override val client: HttpClient
) : BaseCallbackHandler(query, client)
