package ru.raysmith.tgbot.core.handler

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.ChatIdHolder
import ru.raysmith.tgbot.core.IEditor
import ru.raysmith.tgbot.core.ISender
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.BotFeature
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

@HandlerDsl
interface EventHandler : ChatIdHolder, IEditor, ISender {
    var handled: Boolean
    suspend fun setupFeatures(vararg features: BotFeature, callFirst: Boolean = false)
    suspend fun handle()
}

abstract class BaseEventHandler : EventHandler {

    @Deprecated("Always null the because business connection id cannot be obtain from the event")
    override var businessConnectionId: String? = null
    
    override var handled: Boolean = false
    protected val localFeatures: MutableList<BotFeature> = mutableListOf()

    override suspend fun setupFeatures(vararg features: BotFeature, callFirst: Boolean) {
        if (callFirst) localFeatures.addAll(0, features.toList())
        else localFeatures.addAll(features)
    }

    protected suspend fun handleLocalFeatures(handled: Boolean) {
        localFeatures.forEach { feature ->
            feature.handle(this as EventHandler, handled)
        }
    }
}

interface LocationHandler<T : LocationConfig> : EventHandler {
    val update: Update
    val locationsWrapper: LocationsWrapper<T> // TODO should be internal

    context(ctx: BotContext<*>)
    suspend fun toLocation(name: String) {
        val location = locationsWrapper.onToLocation(config, name)
        location.onEnter(config, ctx, this)
    }
    
    val config: T
    fun config() = locationsWrapper.configCreator(locationsWrapper, update)
}