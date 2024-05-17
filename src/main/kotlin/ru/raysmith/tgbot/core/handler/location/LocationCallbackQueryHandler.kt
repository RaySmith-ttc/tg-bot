package ru.raysmith.tgbot.core.handler.location

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.core.handler.HandlerDsl
import ru.raysmith.tgbot.core.handler.LocationHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.utils.BotFeature
import ru.raysmith.tgbot.utils.locations.LocationConfig
import ru.raysmith.tgbot.utils.locations.LocationsWrapper

data class LocationCallbackQueryHandlerData<T : LocationConfig>(
    val handler: (suspend context(T) LocationCallbackQueryHandler<T>.() -> Unit)? = null,
    val features: MutableList<BotFeature>,
    val alwaysAnswer: Boolean
)

@HandlerDsl
open class LocationCallbackQueryHandler<T : LocationConfig>(
    override val update: Update, bot: Bot,
    protected val handlerData: Map<String, LocationCallbackQueryHandlerData<T>>,
    override val locationsWrapper: LocationsWrapper<T>
) : CallbackQueryHandler(update.callbackQuery!!, emptyMap(), bot), LocationHandler<T> {
    override val botConfig: BotConfig = bot.botConfig
    override val config by lazy { config() }

    override suspend fun handle() {
        if (query.data == CallbackQuery.EMPTY_CALLBACK_DATA && !bot.botConfig.ignoreEmptyCallbackData) { answer() }

        for (data in handlerData) {
            if (handled) {
                break
            }

            data.value.handler?.let { h -> h(config, this) }
        }

        for (data in handlerData) {
            if (data.value.features.isNotEmpty()) {
                for (feat in data.value.features) {
                    feat.handle(this, handled)
                }
            }
        }

        handleLocalFeatures(handled)

        if (!handled && handlerData.any { it.value.alwaysAnswer }) {
            answer()
        }
    }
    override suspend fun <R> withBot(bot: Bot, block: suspend BotContext<CallbackQueryHandler>.() -> R): R {
        return LocationCallbackQueryHandler(update, bot, handlerData, locationsWrapper).block()
    }
}