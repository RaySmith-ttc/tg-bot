package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.network.BooleanResponse
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramApi

/** Base implementation of query callback handler */
open class BaseCallbackHandler(open val query: CallbackQuery) : ICallbackHandler {
    var isAnswered = false

    override fun answer(init: AnswerCallbackQuery.() -> Unit): Response<BooleanResponse> {
        return AnswerCallbackQuery().apply(init).let {
            TelegramApi.service.answerCallbackQuery(
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
        }

    }
}