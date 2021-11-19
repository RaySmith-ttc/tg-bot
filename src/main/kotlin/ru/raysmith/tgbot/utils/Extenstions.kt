package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.core.CallbackQueryHandlerData
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi

internal fun List<UpdateType>.asParameter(): String {
    return TelegramApi.json.encodeToJsonElement(ListSerializer(UpdateType.serializer()), this).toString()
}

internal fun <T> MutableList<T>.addIfNotContains(el: T) {
    if (!this.contains(el)) add(el)
}

fun Map<*, CallbackQueryHandlerData>.handleAll(handler: CallbackQueryHandler) {
    forEach {
        it.value.handler?.invoke(handler) ?: it.value.datePicker?.handle(handler)
    }
}