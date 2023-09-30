package ru.raysmith.tgbot.core.handler.base

import io.ktor.client.*
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.datepicker.DatePickerData
import java.time.LocalDate

data class DatePickerCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    val prefix: String,
    override val client: HttpClient
) : BaseCallbackHandler(query, client) {

    val datePickerData = DatePickerData.from(value)

    fun getDate(): LocalDate {
        return LocalDate.of(datePickerData.y!!, datePickerData.m!!, datePickerData.d!!)
    }
}