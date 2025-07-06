package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.InlineQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationInlineQueryHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationInlineQueryHandler<LFC>.() -> Unit)? = null
)

class LocationInlineQueryHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationInlineQueryHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : InlineQueryHandler(update.inlineQuery!!, bot), LocationHandler<LFC, InlineQueryHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<InlineQueryHandler>.() -> R): R {
        return LocationInlineQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}