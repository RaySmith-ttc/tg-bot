package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig

data class CallbackQueryHandlerData(
    val handler: (CallbackQueryHandler.() -> Unit)? = null,
    val datePicker: DatePicker? = null
)

data class LocationCommendHandlerData<T : LocationConfig>(
    val handler: (LocationCommandHandler<T>.(locationConfig: T) -> Unit)? = null
)