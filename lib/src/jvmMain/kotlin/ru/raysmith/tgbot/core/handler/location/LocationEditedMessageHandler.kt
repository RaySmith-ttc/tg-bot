package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.EditedMessageHandler
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationEditMessageHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationEditedMessageHandler<LFC>.() -> Unit)? = null
)

class LocationEditedMessageHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    private val handlerData: MutableList<LocationEditMessageHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : EditedMessageHandler(update.editedMessage!!, bot), LocationHandler<LFC, EditedMessageHandler> {

    override val locationFlowContext by lazy { locationFlowContext() }
    override suspend fun handle() {
        handlerData.forEach {
            it.handler?.let { it1 -> it1(locationFlowContext, this) }?.also {
                handled = true
            }
        }
        handleLocalFeatures(handled)
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<EditedMessageHandler>.() -> R): R {
        return LocationEditedMessageHandler(update, bot, handlerData, locationsWrapper).block()
    }
}