package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

interface EventHandler : ChatIdHolder, IEditor, ISender {
    suspend fun handle()
}

interface LocationHandler<T : LocationConfig> : EventHandler {
    val update: Update
    val locationsWrapper: LocationsWrapper<T>
    fun BotContext<*>.toLocation(name: String) {
        val location = locationsWrapper.onToLocation(config, name)
        location.onEnter(config, this, this@LocationHandler)
    }
    
    val config: T
    fun config() = locationsWrapper.configCreator(update)
}