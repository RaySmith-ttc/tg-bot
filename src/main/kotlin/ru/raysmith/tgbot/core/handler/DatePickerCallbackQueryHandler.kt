package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BaseCallbackHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.datepicker.DatePickerData
import java.time.LocalDate

data class DatePickerCallbackQueryHandler(override val query: CallbackQuery, val value: String, val prefix: String) : BaseCallbackHandler(query) {
    fun getDate(): LocalDate {
        return DatePickerData.from(value).let {
            LocalDate.of(it.y!!, it.m!!, it.d!!)
        }
    }
}