package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update

@DslMarker
annotation class LocationsDSL

@DslMarker
annotation class LocationsDSLConfig

interface LocationConfig {
    val update: Update
}

/** Empty implementation of LocationConfig */
class LocationConfigImpl(override val update: Update) : LocationConfig

@LocationsDSL
class Location<T : LocationConfig>(internal val handlerFactory: LocationEventHandlerFactory<T>) {
    internal lateinit var update: Update
    
    var name: String = ""
        internal set
    
    internal var onEnter: EventHandler.() -> Unit = {  }
    
    @LocationsDSLConfig
    fun onEnter(block: EventHandler.() -> Unit) {
        this.onEnter = block
    }
    fun EventHandler.onEnter() {
        onEnter.invoke(this)
    }
    
    @LocationsDSLConfig
    fun handle(block: LocationEventHandlerFactory<T>.(location: Location<T>) -> Unit) {
        block(handlerFactory, this)
    }
}

@LocationsDSL
private fun <T : LocationConfig> location(wrapper: LocationsWrapper<T>, setup: LocationEventHandlerFactory<T>.(location: Location<T>) -> Unit): Location<T> {
    val factory = LocationEventHandlerFactory(wrapper)
    val location = Location<T>(factory)
    factory.setup(location)
    return location
}

@LocationsDSL
class LocationsWrapper<T : LocationConfig> {
    internal val locations = mutableMapOf<String, Location<T>>()
    
    private var locationNameGetter: T.() -> String = { error("TODO") }
    @LocationsDSLConfig fun getLocation(locationNameGetter: T.() -> String) {
        this.locationNameGetter = locationNameGetter
    }
    
    internal var configCreator: (update: Update) -> T = { error("TODO") }
    @LocationsDSLConfig fun config(configCreator: (update: Update) -> T) {
        this.configCreator = configCreator
    }
    
    private var updateLocation: T.(location: Location<T>) -> Unit = {  }
    @LocationsDSLConfig fun updateLocation(block: T.(location: Location<T>) -> Unit) {
        this.updateLocation = block
    }
    
    internal var onToLocation: T.(name: String) -> Location<T> = {
        val location = locations[it] ?: error("TODO")
        updateLocation(this, location)
        location
    }
    
    fun add(location: Location<T>) {
        locations[location.name] = location
    }
    
    private val additionalEventHandlers: MutableMap<String, LocationEventHandlerFactory<T>.(T) -> Unit> = mutableMapOf()
    @LocationsDSLConfig fun global(
        handlersId: String = CallbackQueryHandler.GLOBAL_HANDLER_ID,
        setup: LocationEventHandlerFactory<T>.(config: T) -> Unit
    ) {
        additionalEventHandlers[handlersId] = setup
    }
    
    @LocationsDSL
    fun location(name: String, newLocation: Location<T>.() -> Unit) {
        add(location(this) {
            it.name = name
            newLocation(it)
        })
    }
    
    internal fun getHandlerFactory(update: Update): LocationEventHandlerFactory<T> {
        val config = configCreator(update)
        val location = locations[locationNameGetter(config)] ?: error("TODO")
        config.updateLocation(location)
        return location.handlerFactory.apply {
            additionalEventHandlers.forEach { (handlerId, handler) ->
                withHandlerId(handlerId) {
                    handler(config)
                }
            }
        }
    }
}