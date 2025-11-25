package ru.raysmith.tgbot.examples.locations

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import java.util.Locale

val birthDateDatePicker = DatePicker(CallbackQuery.DP_BIRTH_DATE_PREFIX) {
    messageText { _, _, _ ->
        text("Please, select your birth date")
    }
    locale(Locale.forLanguageTag("en"))
    additionalRows {
        back()
    }
}