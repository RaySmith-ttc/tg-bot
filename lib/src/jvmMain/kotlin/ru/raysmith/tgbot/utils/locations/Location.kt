package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.ILocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.model.network.updates.Update

@LocationsDSL
suspend fun <T : LocationConfig> createLocation(name: String, wrapper: LocationsWrapper<T>, setup: suspend LocationEventHandlerFactory<T>.(location: Location<T>) -> Unit): Location<T> {
    val factory = LocationEventHandlerFactory(wrapper)
    val location = Location(name, factory)
    factory.setup(location)
    return location
}

@LocationsDSL
class Location<T : LocationConfig>(val name: String, internal val handlerFactory: LocationEventHandlerFactory<T>) : ILocationEventHandlerFactory<T> by handlerFactory {
    internal var isAdditionalHandlersInit = false
    internal lateinit var update: Update
    
    var onEnter: suspend context(T, BotContext<*>) LocationHandler<T>.() -> Unit = {  }
        private set
    
    @LocationsDSL
    fun onEnter(block: suspend context(T, BotContext<*>) LocationHandler<T>.() -> Unit) {
        this.onEnter = block
    }
    
    @LocationsDSL
    @Deprecated("This DSL can be omitted", ReplaceWith("block()"))
    suspend fun handle(block: suspend LocationEventHandlerFactory<T>.() -> Unit) {
        block(handlerFactory)
    }
}