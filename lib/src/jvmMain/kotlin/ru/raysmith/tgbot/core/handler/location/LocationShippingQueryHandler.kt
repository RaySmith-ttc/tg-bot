package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ShippingQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationShippingQueryHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationShippingQueryHandler<LFC>.() -> Unit)? = null
)

class LocationShippingQueryHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationShippingQueryHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : ShippingQueryHandler(update.shippingQuery!!, bot), LocationHandler<LFC, ShippingQueryHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ShippingQueryHandler>.() -> R): R {
        return LocationShippingQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}