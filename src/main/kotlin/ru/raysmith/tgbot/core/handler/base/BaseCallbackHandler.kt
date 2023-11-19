package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.core.handler.ICallbackHandler
import ru.raysmith.tgbot.model.bot.AnswerCallbackQuery
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.API

/** Base implementation of query callback handler */
abstract class BaseCallbackHandler(open val query: CallbackQuery, override val client: HttpClient) :
    ICallbackHandler, API {
    var isAnswered = false

    suspend fun answer(text: String) = answer { this.text = text }
    suspend fun alert(text: String) = answer { this.text = text; showAlert = true }

    override suspend fun answer(init: AnswerCallbackQuery.() -> Unit): Boolean {
        return AnswerCallbackQuery().apply(init).let {
            answerCallbackQuery(
                callbackQueryId = query.id,
                text = it.text,
                showAlert = it.showAlert,
                url = it.url,
                cacheTime = it.cacheTime
            ).also {
                isAnswered = true
            }
        }
    }
}