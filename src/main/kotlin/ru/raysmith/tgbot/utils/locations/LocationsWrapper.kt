package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@LocationsDSL
class LocationsWrapper<L : LocationConfig> {
    internal val locations = mutableMapOf<String, Location<L>>()
    
    private var locationNameGetter: L.() -> String = { error("TODO") }
    @LocationsDSLConfig
    fun getLocation(locationNameGetter: L.() -> String) {
        this.locationNameGetter = locationNameGetter
    }
    
    internal var configCreator: (update: Update) -> L = { error("TODO") }
    @LocationsDSLConfig
    fun config(configCreator: (update: Update) -> L) {
        this.configCreator = configCreator
    }
    
    private var updateLocation: L.(location: Location<L>) -> Unit = {  }
    @LocationsDSLConfig
    fun updateLocation(block: L.(location: Location<L>) -> Unit) {
        this.updateLocation = block
    }
    
    private var filter: L.(update: Update) -> Boolean = { true }
    @LocationsDSLConfig
    fun filter(block: L.(update: Update) -> Boolean) {
        this.filter = block
    }
    
    internal var onToLocation: L.(name: String) -> Location<L> = {
        val location = locations[it] ?: error("TODO")
        updateLocation(this, location)
        location
    }
    
    fun add(location: Location<L>) {
        locations[location.name] = location
    }
    
    private val additionalEventHandlers: MutableMap<String, LocationEventHandlerFactory<L>.(L) -> Unit> = mutableMapOf()
    @LocationsDSLConfig
    fun global(
        handlersId: String = CallbackQueryHandler.GLOBAL_HANDLER_ID,
        setup: LocationEventHandlerFactory<L>.(config: L) -> Unit
    ) {
        additionalEventHandlers[handlersId] = setup
    }
    
    @LocationsDSL
    fun location(name: String, @LocationsDSL newLocation: Location<L>.() -> Unit) {
        add(createLocation(name, this) {
            newLocation(it)
        })
    }
    
    fun allowedUpdates(): Set<UpdateType> {
        val config = configCreator(Update(0))
        val locationUpdates = locations.map { it.value.handlerFactory.allowedUpdates }.flatten()
        val location = Location("", LocationEventHandlerFactory<L>(LocationsWrapper()))
        val globalUpdates = location.handlerFactory.apply {
            additionalEventHandlers.forEach { (handlerId, handler) ->
                withHandlerId(handlerId) {
                    handler(config)
                }
            }
        }.allowedUpdates
        
        return (locationUpdates + globalUpdates).toSet()
    }
    
    fun LocationEventHandlerFactory<L>.withAdditionalHandlers(config: L): LocationEventHandlerFactory<L> {
        additionalEventHandlers.forEach { (handlerId, handler) ->
            withHandlerId(handlerId) {
                handler(config)
            }
        }
        
        return this
    }
    
    internal fun getHandlerFactory(update: Update): LocationEventHandlerFactory<L> {
        val config = configCreator(update)
        if (!filter.invoke(config, update)) {
            Bot.logger.debug("Update #${update.updateId} skipped by locations filter")
            return LocationEventHandlerFactory(this)
        }
        
        val location = locations[locationNameGetter(config)] ?: return LocationEventHandlerFactory(this)
            .withAdditionalHandlers(config)
        
        config.updateLocation(location)
        return location.handlerFactory.withAdditionalHandlers(config)
    }
}