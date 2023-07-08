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
    fun toLocation(name: String) {
        val location = locationsWrapper.onToLocation(config, name)
        location.onEnter(config, this)
    }
    
    val config: T
    fun config() = locationsWrapper.configCreator(update)
}