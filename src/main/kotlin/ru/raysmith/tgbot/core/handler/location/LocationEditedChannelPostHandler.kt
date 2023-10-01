package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.EditedChannelPostHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationEditedChannelPostHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationEditedChannelPostHandler<T>.() -> Unit)? = null
)

@HandlerDsl
open class LocationEditedChannelPostHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: Map<String, LocationEditedChannelPostHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : EditedChannelPostHandler(update.message!!, client), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }
        }
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedChannelPostHandler>.() -> R): R {
        return LocationEditedChannelPostHandler(update, bot.client, handlerData, locationsWrapper).block()
    }
}