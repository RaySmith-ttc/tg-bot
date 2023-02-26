package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.utils.locations.LocationConfig

data class LocationMessageHandlerData<T : LocationConfig>(
    val handler: (LocationMessageHandler<T>.(locationConfig: T) -> Unit)? = null
)

