package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.locations.LocationConfig

data class LocationCallbackQueryHandlerData<T : LocationConfig>(
    val handler: (LocationCallbackQueryHandler<T>.(locationConfig: T) -> Unit)? = null,
    val datePicker: DatePicker? = null
)