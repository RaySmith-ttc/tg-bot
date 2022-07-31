package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

data class ValueDataCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : BaseCallbackHandler(query, service, fileService)
