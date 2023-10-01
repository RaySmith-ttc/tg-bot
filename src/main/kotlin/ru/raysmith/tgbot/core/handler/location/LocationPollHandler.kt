package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.PollHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationPollHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationPollHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationPollHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: MutableMap<String, LocationPollHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : PollHandler(update.poll!!, client), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<PollHandler>.() -> R): R {
        return LocationPollHandler(update, bot.client, handlerData, locationsWrapper).block()
    }
}