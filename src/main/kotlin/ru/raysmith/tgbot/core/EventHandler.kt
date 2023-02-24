package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

interface EventHandler : ChatIdHolder, IEditor, ISender {
    suspend fun handle()
}

interface LocationHandler<T : LocationConfig> : EventHandler {
    val update: Update
    val locationsWrapper: LocationsWrapper<T>
    fun toLocation(name: String)
    
    fun config() = locationsWrapper.configCreator(update)
}