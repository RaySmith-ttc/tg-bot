package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PreCheckoutQueryHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPreCheckoutQueryHandlerData<T : LocationConfig>(
    val handler: (context(T) LocationPreCheckoutQueryHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationPreCheckoutQueryHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: MutableMap<String, LocationPreCheckoutQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : PreCheckoutQueryHandler(update.preCheckoutQuery!!, client), LocationHandler<T> {

    override val config by lazy { config() }
    override fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override fun <R> withBot(bot: Bot, block: BotContext<PreCheckoutQueryHandler>.() -> R): R {
        return LocationPreCheckoutQueryHandler(update, bot.client, handlerData, locationsWrapper).block()
    }
}