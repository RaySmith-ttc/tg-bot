package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.handler.ILocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.model.network.updates.Update

@LocationsDSL
suspend fun <LFC : LocationFlowContext> createLocation(name: String, wrapper: LocationsWrapper<LFC>, setup: suspend LocationEventHandlerFactory<LFC>.(location: Location<LFC>) -> Unit): Location<LFC> {
    val factory = LocationEventHandlerFactory(wrapper)
    val location = Location(name, factory)
    factory.setup(location)
    return location
}

@LocationsDSL
class Location<LFC : LocationFlowContext>(val name: String, internal val handlerFactory: LocationEventHandlerFactory<LFC>) : ILocationEventHandlerFactory<LFC> by handlerFactory {
    internal var isAdditionalHandlersInit = false
    internal lateinit var update: Update
    
    var onEnter: suspend context(LFC) LocationHandler<LFC, *>.() -> Unit = {  }
        private set
    
    @LocationsDSL
    fun onEnter(block: suspend context(LFC) LocationHandler<LFC, *>.() -> Unit) {
        this.onEnter = block
    }
    
    @LocationsDSL
    @Deprecated("This DSL can be omitted", ReplaceWith("block()"))
    suspend fun handle(block: suspend LocationEventHandlerFactory<LFC>.() -> Unit) {
        block(handlerFactory)
    }
}