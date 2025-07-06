package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.locations.LocationFlowContext
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationCallbackQueryHandlerData<LFC : LocationFlowContext>(
    val handler: (suspend context(LFC) LocationCallbackQueryHandler<LFC>.() -> Unit)? = null,
    val alwaysAnswer: Boolean
)

open class LocationCallbackQueryHandler<LFC : LocationFlowContext>(
    override val update: Update, bot: Bot,
    protected val handlerData: List<LocationCallbackQueryHandlerData<LFC>>,
    override val locationsWrapper: LocationsWrapper<LFC>
) : CallbackQueryHandler(update.callbackQuery!!, emptyList(), bot), LocationHandler<LFC, CallbackQueryHandler> {
    override val botConfig: BotConfig = bot.botConfig
    override val locationFlowContext by lazy { locationFlowContext() }

//    init {
//        println("init LocationCallbackQueryHandler")
//        localFeatures.addAll(bot.botConfig.defaultCallbackQueryHandlerFeatures.toTypedArray())
//    }

    override suspend fun handle() {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA && bot.botConfig.ignoreEmptyCallbackData) { answer() }

        for (data in handlerData) {
            if (handled) {
                break
            }

            data.handler?.let { h -> h(locationFlowContext, this) }
        }

        handleLocalFeatures(handled)

        if (!handled && handlerData.any { it.alwaysAnswer }) {
            answer()
        }
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CallbackQueryHandler>.() -> R): R {
        return LocationCallbackQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}