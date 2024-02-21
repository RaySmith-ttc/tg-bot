package ru.raysmith.tgbot.core.handler.location

import io.ktor.client.*
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.EditedMessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationEditMessageHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationEditedMessageHandler<T>.() -> Unit)? = null
)

@HandlerDsl
class LocationEditedMessageHandler<T : LocationConfig>(
    override val update: Update, client: HttpClient,
    private val handlerData: MutableMap<String, LocationEditMessageHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : EditedMessageHandler(update.editedMessage!!, client), LocationHandler<T> {

    override val config by lazy { config() }
    override suspend fun handle() {
        handlerData.forEach {
            it.value.handler?.let { it1 -> it1(config, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedMessageHandler>.() -> R): R {
        return LocationEditedMessageHandler(update, bot.client, handlerData, locationsWrapper).block()
    }
}