package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.LocationHandler
import ru.raysmith.tgbot.model.network.updates.Update

@LocationsDSL
fun <T : LocationConfig> createLocation(name: String, wrapper: LocationsWrapper<T>, setup: LocationEventHandlerFactory<T>.(location: Location<T>) -> Unit): Location<T> {
    val factory = LocationEventHandlerFactory(wrapper)
    val location = Location(name, factory)
    factory.setup(location)
    return location
}

@LocationsDSL
class Location<T : LocationConfig>(val name: String, internal val handlerFactory: LocationEventHandlerFactory<T>) {
    internal lateinit var update: Update
    
    var onEnter: context(T, BotContext<*>) LocationHandler<T>.() -> Unit = {  }
        private set
    
    @LocationsDSL
    fun onEnter(block: context(T, BotContext<*>) LocationHandler<T>.() -> Unit) {
        this.onEnter = block
    }
    
    @LocationsDSL
    fun handle(block: LocationEventHandlerFactory<T>.() -> Unit) {
        block(handlerFactory)
    }
}