package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PollAnswerHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPollAnswerHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationPollAnswerHandler<LFC>.() -> Unit)? = null
)

class LocationPollAnswerHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationPollAnswerHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : PollAnswerHandler(update.pollAnswer!!, bot), LocationHandler<LFC, PollAnswerHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollAnswerHandler>.() -> R): R {
        return LocationPollAnswerHandler(update, bot, handlerData, locationsWrapper).block()
    }
}