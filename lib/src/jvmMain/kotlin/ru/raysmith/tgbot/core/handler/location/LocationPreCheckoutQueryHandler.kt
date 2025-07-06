package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PreCheckoutQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPreCheckoutQueryHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationPreCheckoutQueryHandler<LFC>.() -> Unit)? = null
)

class LocationPreCheckoutQueryHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationPreCheckoutQueryHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : PreCheckoutQueryHandler(update.preCheckoutQuery!!, bot), LocationHandler<LFC, PreCheckoutQueryHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return LocationPreCheckoutQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}