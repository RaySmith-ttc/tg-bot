package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.datepicker.DatePickerData
import java.time.LocalDate

data class DatePickerCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    val prefix: String,
    override val service: TelegramService
) : BaseCallbackHandler(query, service) {

    val datePickerData = DatePickerData.from(value)

    fun getDate(): LocalDate {
        return LocalDate.of(datePickerData.y!!, datePickerData.m!!, datePickerData.d!!)
    }
}