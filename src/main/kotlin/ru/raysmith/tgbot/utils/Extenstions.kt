package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.core.handler.CallbackQueryHandlerData
import ru.raysmith.tgbot.core.handler.MessageHandler
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.media.PhotoSize
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

internal fun Properties?.getOrDefault(key: String, default: String) = this?.getOrDefault(key, default)?.toString() ?: default

/** Append line break */
fun MessageText.n() = text("\n")

fun String.withSafeLength(type: MessageTextType) = if (this.length > type.maxLength) this.take(type.maxLength) else this

fun Long.toChatId(): ChatId.ID = ChatId.of(this) as ChatId.ID
fun String.toChatId(): ChatId.Username = ChatId.of(this) as ChatId.Username

/**
 * Applies a [block] to the message text after [conversion] to another type or returns null if it gets null at any stage
 * */
fun <T> MessageHandler.messageText(conversion: (String) -> T?, block: (T) -> Unit): T? {
    return message.text?.let(conversion)?.also(block)
}

/**
 * Applies a [block] to the message text after [verification] or returns null if it gets null at any stage
 * */
fun MessageHandler.messageText(verification: (String) -> String? = { it }, block: (String) -> Unit): String? =
    messageText<String>(verification, block)

// TODO add other media
/**
 * Applies a [block] to the largest size of sent photo or returns null if it gets null at any stage
 * */
fun MessageHandler.messagePhoto(block: (PhotoSize) -> Unit): Any? {
    return message.photo?.last()?.also(block)
}