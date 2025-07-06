package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PollHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPollHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationPollHandler<LFC>.() -> Unit)? = null
)

class LocationPollHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationPollHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : PollHandler(update.poll!!, bot), LocationHandler<LFC, PollHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollHandler>.() -> R): R {
        return LocationPollHandler(update, bot, handlerData, locationsWrapper).block()
    }
}