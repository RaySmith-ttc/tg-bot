package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.EventHandler
import ru.raysmith.tgbot.core.EventHandlerFactory
import ru.raysmith.tgbot.core.LocationEventHandlerFactory
import ru.raysmith.tgbot.model.network.updates.Update

@DslMarker
annotation class LocationsDSL

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
    fun onEnter(block: EventHandler.() -> Unit) {
        this.onEnter = block
    }
    
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
    fun getLocation(locationNameGetter: T.() -> String) {
        this.locationNameGetter = locationNameGetter
    }
    
    internal var configCreator: (update: Update) -> T = { error("TODO") }
    fun config(configCreator: (update: Update) -> T) {
        this.configCreator = configCreator
    }
    
    private var updateLocation: T.(location: Location<T>) -> Unit = {  }
    fun updateLocation(block: T.(location: Location<T>) -> Unit) {
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
    
    private val additionalEventHandlers: MutableList<LocationEventHandlerFactory<T>.(T) -> Unit> = mutableListOf()
    fun global(setup: LocationEventHandlerFactory<T>.(config: T) -> Unit) {
        additionalEventHandlers.add(setup)
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
            additionalEventHandlers.forEach {
                apply { it(config) }
            }
        }
    }
}