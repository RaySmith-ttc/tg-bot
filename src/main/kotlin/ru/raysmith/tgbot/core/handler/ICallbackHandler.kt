package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.bot.AnswerCallbackQuery

/** Represent callback handler that can answer to query */
interface ICallbackHandler {

    /** Call [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) method without additional parameters */
    suspend fun answer() = answer {  }

    /** Call [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) with build parameters */
    suspend fun answer(init: AnswerCallbackQuery.() -> Unit): Boolean
}