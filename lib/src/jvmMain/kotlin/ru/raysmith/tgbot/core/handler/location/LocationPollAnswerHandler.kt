package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PollAnswerHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPollAnswerHandlerData<T : LocationFlowContext>(
    val handler: (suspend context(T) LocationPollAnswerHandler<T>.() -> Unit)? = null
)

class LocationPollAnswerHandler<T : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationPollAnswerHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : PollAnswerHandler(update.pollAnswer!!, bot), LocationHandler<T, PollAnswerHandler> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollAnswerHandler>.() -> R): R {
        return LocationPollAnswerHandler(update, bot, handlerData, locationsWrapper).block()
    }
}