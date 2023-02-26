package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.utils.locations.LocationConfig

data class LocationCommandHandlerData<T : LocationConfig>(
    val handler: (LocationCommandHandler<T>.(locationConfig: T) -> Unit)? = null
)