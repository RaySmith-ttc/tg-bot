package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.handler.base.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

data class PaginationCallbackQueryHandler(
    override val query: CallbackQuery,
    val page: Long,
    override val service: TelegramService,
    override val fileService: TelegramFileService
) : BaseCallbackHandler(query, service, fileService)