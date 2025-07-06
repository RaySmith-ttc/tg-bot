package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@LocationsDSL
class LocationsWrapper<LFC : LocationFlowContext>(override val bot: Bot) : BotHolder {
    internal val locations = mutableMapOf<String, Location<LFC>>()
    
    private var locationNameGetter: LFC.() -> String = { error("TODO") } // TODO
    fun getLocation(locationNameGetter: LFC.() -> String) {
        this.locationNameGetter = locationNameGetter
    }

    internal var configCreator: (update: Update) -> LFC = { error("TODO") } // TODO
    fun config(configCreator: (update: Update) -> LFC) {
        this.configCreator = configCreator
    }

    private var updateLocation: LFC.(location: Location<LFC>) -> Unit = {  }
    fun updateLocation(block: LFC.(location: Location<LFC>) -> Unit) {
        this.updateLocation = block
    }
    
    private var filter: context(LFC) LocationsWrapper<LFC>.(update: Update) -> Boolean = { true }
    fun filter(block: context(LFC) LocationsWrapper<LFC>.(update: Update) -> Boolean) {
        this.filter = block
    }
    
    internal var onToLocation: LFC.(name: String) -> Location<LFC> = {
        val location = locations[it] ?: error("TODO") // TODO
        updateLocation(location)
        location
    }
    
    private fun add(location: Location<LFC>) {
        locations[location.name] = location
    }
    
    private val additionalEventHandlers: MutableList<suspend LocationEventHandlerFactory<LFC>.() -> Unit> = mutableListOf()
    fun global(setup: suspend LocationEventHandlerFactory<LFC>.() -> Unit) { // TODO: DSL scope violation
        additionalEventHandlers.add(setup)
    }

    suspend fun location(name: String, newLocation: suspend Location<LFC>.() -> Unit) {
        add(createLocation(name, this) {
            newLocation(it)
        }.also { it.handlerFactory })
    }

    suspend fun allowedUpdates(bot: Bot): Set<UpdateType> {
        val locationUpdates = locations.map { it.value.handlerFactory.allowedUpdates }.flatten()
        val location = Location("", LocationEventHandlerFactory<LFC>(LocationsWrapper(bot)))
        val globalUpdates = location.handlerFactory.apply {
            additionalEventHandlers.forEach { handler ->
                handler()
            }
        }.allowedUpdates
        
        return (locationUpdates + globalUpdates).toSet()
    }

    private suspend fun LocationEventHandlerFactory<LFC>.withAdditionalHandlers(): LocationEventHandlerFactory<LFC> {
        additionalEventHandlers.forEach { handler ->
            handler()
        }
        
        return this
    }

    internal suspend fun getHandlerFactory(
        update: Update,
        additionalEventHandlers: MutableList<(eventHandlerFactory: EventHandlerFactory) -> Unit>
    ): LocationEventHandlerFactory<LFC> {
        val config = configCreator(update)
        if (!filter.invoke(config, this, update)) {
            Bot.logger.debug("Update #${update.updateId} skipped by locations filter")
            return LocationEventHandlerFactory(this)
        }

        val locationName = locationNameGetter(config)
        val location = locations[locationName] ?: return LocationEventHandlerFactory(this)
            .withAdditionalHandlers()
        
        updateLocation(config, location)

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