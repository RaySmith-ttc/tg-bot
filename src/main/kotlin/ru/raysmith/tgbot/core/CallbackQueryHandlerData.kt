package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.utils.datepicker.DatePicker

data class CallbackQueryHandlerData(
    val handler: (CallbackQueryHandler.() -> Unit)? = null,
    val datePicker: DatePicker? = null
)