package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@LocationsDSL
class LocationsWrapper<L : LocationFlowContext>(override val bot: Bot) : BotHolder {
    internal val locations = mutableMapOf<String, Location<L>>()
    
    private var locationNameGetter: context(L) LocationsWrapper<L>.() -> String = { error("TODO") } // TODO
    fun getLocation(locationNameGetter: context(L, LocationsWrapper<L>) () -> String) {
        this.locationNameGetter = locationNameGetter
    }

    internal var configCreator: context(LocationsWrapper<L>) (update: Update) -> L = { error("TODO") } // TODO
    fun config(configCreator: context(LocationsWrapper<L>) (update: Update) -> L) {
        this.configCreator = configCreator
    }

    private var updateLocation: context(LocationsWrapper<L>) (location: Location<L>) -> Unit = {  }
    fun updateLocation(block: context(LocationsWrapper<L>) (location: Location<L>) -> Unit) {
        this.updateLocation = block
    }
    
    private var filter: context(L) LocationsWrapper<L>.(update: Update) -> Boolean = { true }
    fun filter(block: context(L) LocationsWrapper<L>.(update: Update) -> Boolean) {
        this.filter = block
    }
    
    internal var onToLocation: L.(name: String) -> Location<L> = {
        val location = locations[it] ?: error("TODO") // TODO
        updateLocation(location)
        location
    }
    
    private fun add(location: Location<L>) {
        locations[location.name] = location
    }
    
    private val additionalEventHandlers: MutableList<suspend LocationEventHandlerFactory<L>.() -> Unit> = mutableListOf()
    fun global(setup: suspend LocationEventHandlerFactory<L>.() -> Unit) { // TODO: DSL scope violation
        additionalEventHandlers.add(setup)
    }

    suspend fun location(name: String, newLocation: suspend Location<L>.() -> Unit) {
        add(createLocation(name, this) {
            newLocation(it)
        }.also { it.handlerFactory })
    }

    suspend fun allowedUpdates(bot: Bot): Set<UpdateType> {
        val locationUpdates = locations.map { it.value.handlerFactory.allowedUpdates }.flatten()
        val location = Location("", LocationEventHandlerFactory<L>(LocationsWrapper(bot)))
        val globalUpdates = location.handlerFactory.apply {
            additionalEventHandlers.forEach { handler ->
                handler()
            }
        }.allowedUpdates
        
        return (locationUpdates + globalUpdates).toSet()
    }

    private suspend fun LocationEventHandlerFactory<L>.withAdditionalHandlers(): LocationEventHandlerFactory<L> {
        additionalEventHandlers.forEach { handler ->
            handler()
        }
        
        return this
    }

    internal suspend fun getHandlerFactory(
        update: Update,
        additionalEventHandlers: MutableList<(eventHandlerFactory: EventHandlerFactory) -> Unit>
    ): LocationEventHandlerFactory<L> {
        val config = configCreator(update)
        if (!filter.invoke(config, this, update)) {
            Bot.logger.debug("Update #${update.updateId} skipped by locations filter")
            return LocationEventHandlerFactory(this)
        }

        val locationName = locationNameGetter(config, this)
        val location = locations[locationName] ?: return LocationEventHandlerFactory(this)
            .withAdditionalHandlers()
        
        updateLocation(location)

        if (!location.isAdditionalHandlersInit) {
            location.handlerFactory.withAdditionalHandlers()
            additionalEventHandlers.forEach {
                location.handlerFactory.apply(it)
            }

            location.isAdditionalHandlersInit = true
        }

        return location.handlerFactory
    }
}