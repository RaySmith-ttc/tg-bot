package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.CallbackQueryHandlerData
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.MessageText
import ru.raysmith.tgbot.model.bot.MessageTextType
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi
import java.util.*

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

fun String.withSafeLength(type: MessageTextType) = if (this.length > type.maxLength) this.take(type.maxLength) else this

internal fun Properties?.getOrDefault(key: String, default: String) = this?.getOrDefault(key, default)?.toString() ?: default

fun Long.toChatId(): ChatId.ID = ChatId.of(this) as ChatId.ID
fun String.toChatId(): ChatId.Username = ChatId.of(this) as ChatId.Username