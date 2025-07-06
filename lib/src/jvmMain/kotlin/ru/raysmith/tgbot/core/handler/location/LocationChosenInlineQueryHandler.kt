package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.ChosenInlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationChosenInlineQueryHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationChosenInlineQueryHandler<LFC>.() -> Unit)? = null
)

class LocationChosenInlineQueryHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationChosenInlineQueryHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : ChosenInlineQueryHandler(update.chosenInlineResult!!, bot), LocationHandler<LFC, ChosenInlineQueryHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<ChosenInlineQueryHandler>.() -> R): R {
        return LocationChosenInlineQueryHandler(update, bot, handlerData, locationsWrapper).let {
            this.block()
        }
    }
}