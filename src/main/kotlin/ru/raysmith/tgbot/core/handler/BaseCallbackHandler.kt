package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.bot.AnswerCallbackQuery
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Base implementation of query callback handler */
open class BaseCallbackHandler(open val query: CallbackQuery, open val service: TelegramService, open val fileService: TelegramFileService) : ICallbackHandler {
    var isAnswered = false

    fun answer(text: String) = answer { this.text = text }
    fun alert(text: String) = answer { this.text = text; showAlert = true }

    override fun answer(init: AnswerCallbackQuery.() -> Unit): BooleanResponse {
        return AnswerCallbackQuery().apply(init).let {
            service.answerCallbackQuery(
                callbackQueryId = query.id,
                text = it.text,
                showAlert = it.showAlert,
                url = it.url,
                cacheTime = it.cacheTime
            ).execute().also { response ->
                if (response.isSuccessful && response.body()?.result == true) {
                    isAnswered = true
                }
            }
        }.body() ?: errorBody()

    }
}