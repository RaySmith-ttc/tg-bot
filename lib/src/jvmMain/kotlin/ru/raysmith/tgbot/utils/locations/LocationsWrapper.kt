package ru.raysmith.tgbot.utils.locations

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.core.handler.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.LocationEventHandlerFactory
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.model.network.updates.UpdateType

@LocationsDSL
class LocationsWrapper<L : LocationConfig>(override val bot: Bot) : BotHolder {
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
    
    private val additionalEventHandlers: MutableList<suspend LocationEventHandlerFactory<L>.(L) -> Unit> = mutableListOf()
    @LocationsDSLConfig
    fun global(setup: suspend LocationEventHandlerFactory<L>.(config: L) -> Unit) {
        additionalEventHandlers.add(setup)
    }

    context(BotHolder)
    @LocationsDSL
    suspend fun location(name: String, @LocationsDSL newLocation: suspend Location<L>.() -> Unit) {
        add(createLocation(name, this) {
            newLocation(it)
        }.also { it.handlerFactory })
    }

    suspend fun allowedUpdates(bot: Bot): Set<UpdateType> {
        val config = configCreator(Update(0))
        val locationUpdates = locations.map { it.value.handlerFactory.allowedUpdates }.flatten()
        val location = Location("", LocationEventHandlerFactory<L>(LocationsWrapper(bot)))
        val globalUpdates = location.handlerFactory.apply {
            additionalEventHandlers.forEach { handler ->
                handler(config)
            }
        }.allowedUpdates
        
        return (locationUpdates + globalUpdates).toSet()
    }

    private suspend fun LocationEventHandlerFactory<L>.withAdditionalHandlers(config: L): LocationEventHandlerFactory<L> {
        additionalEventHandlers.forEach { handler ->
            handler(config)
        }
        
        return this
    }

    internal suspend fun getHandlerFactory(
        update: Update,
        additionalEventHandlers: MutableList<(eventHandlerFactory: EventHandlerFactory) -> Unit>
    ): LocationEventHandlerFactory<L> {
        val config = configCreator(update)
        if (!filter.invoke(config, update)) {
            Bot.logger.debug("Update #${update.updateId} skipped by locations filter")
            return LocationEventHandlerFactory(this)
        }
        
        val location = locations[locationNameGetter(config)] ?: return LocationEventHandlerFactory(this)
            .withAdditionalHandlers(config)
        
        config.updateLocation(location)

        if (!location.isAdditionalHandlersInit) {
            location.handlerFactory.withAdditionalHandlers(config)
            additionalEventHandlers.forEach {
                location.handlerFactory.apply(it)
            }

            location.isAdditionalHandlersInit = true
        }

        return location.handlerFactory
    }
}