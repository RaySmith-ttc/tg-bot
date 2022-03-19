package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.core.handler.CallbackQueryHandlerData
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.IMessage
import ru.raysmith.tgbot.model.bot.MessageText
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi

internal fun List<UpdateType>.asParameter(): String {
    return TelegramApi.json.encodeToJsonElement(ListSerializer(UpdateType.serializer()), this).toString()
}

internal fun <T> MutableList<T>.addIfNotContains(el: T) {
    if (!this.contains(el)) add(el)
}

internal fun Map<*, CallbackQueryHandlerData>.handleAll(handler: CallbackQueryHandler) {
    for (it in this) {
        if (handler.isAnswered) break

        it.value.datePicker?.handle(handler) ?: it.value.handler?.invoke(handler)
    }
}

/** Append line break */
fun MessageText.n() = text("\n")

fun String.withSafeLength() = this.let {
    if (it.length > IMessage.MAX_TEXT_LENGTH) it.take(IMessage.MAX_TEXT_LENGTH) else it
}