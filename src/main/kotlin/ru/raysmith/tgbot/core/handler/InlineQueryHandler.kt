package ru.raysmith.tgbot.core.handler

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.HandlerDsl
import ru.raysmith.tgbot.model.network.InlineQuery
import ru.raysmith.tgbot.model.network.InlineQueryResult
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

@HandlerDsl
class InlineQueryHandler(
    val inlineQuery: InlineQuery,
    override val service: TelegramService, override val fileService: TelegramFileService,
    private val handler: InlineQueryHandler.() -> Unit
) : EventHandler {
    override var messageId: Int? = null
    override var inlineMessageId: String? = null

    override fun getChatId() = inlineQuery.from.id
    override suspend fun handle() = handler()

    fun answerInlineQuery(
        id: String,
        results: List<InlineQueryResult>,
        cacheTime: Int? = null,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        switchPmText: String? = null,
        switchPmParameter: String? = null,
    ): Boolean {
        return service.answerInlineQuery(
            id, TelegramApi.json.encodeToString(results), cacheTime, isPersonal, nextOffset, switchPmText, switchPmParameter
        ).execute().body()?.result ?: errorBody()
    }
}