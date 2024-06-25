package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PreCheckoutQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPreCheckoutQueryHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationPreCheckoutQueryHandler<T : LocationConfig>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationPreCheckoutQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : PreCheckoutQueryHandler(update.preCheckoutQuery!!, bot), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return LocationPreCheckoutQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}