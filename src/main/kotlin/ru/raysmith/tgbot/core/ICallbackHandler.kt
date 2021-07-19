package ru.raysmith.tgbot.core

import retrofit2.Response
import ru.raysmith.tgbot.model.network.BooleanResponse

/** Represent callback handler that can answer to query */
interface ICallbackHandler {
    /** Call [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) method with no parameters */
    fun answer() = answer {  }

    /** Call [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) with build parameters */
    fun answer(init: AnswerCallbackQuery.() -> Unit): Response<BooleanResponse>
}