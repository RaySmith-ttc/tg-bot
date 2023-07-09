package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.utils.datepicker.DatePicker

data class CallbackQueryHandlerData(
    val handler: (CallbackQueryHandler.() -> Unit)? = null,
    val datePicker: DatePicker? = null,
    val alwaysAnswer: Boolean
)

