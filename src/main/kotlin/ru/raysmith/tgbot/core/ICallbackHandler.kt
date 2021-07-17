package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.network.BooleanResponse

interface ICallbackHandler {
    fun answer() = answer {  }
    fun answer(init: AnswerCallbackQuery.() -> Unit): Response<BooleanResponse>
}