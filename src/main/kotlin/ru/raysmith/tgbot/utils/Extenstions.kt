package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.MessageTextType
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi
import java.util.*

internal fun List<UpdateType>.asParameter(): String {
    return TelegramApi.json.encodeToJsonElement(ListSerializer(UpdateType.serializer()), this).toString()
}

internal fun Properties?.getOrDefault(key: String, default: String) = this?.getOrDefault(key, default)?.toString() ?: default

/** Append line break */
fun MessageText.n() = text("\n")

fun String.withSafeLength(type: MessageTextType) = if (this.length > type.maxLength) this.take(type.maxLength) else this

fun Long.toChatId(): ChatId.ID = ChatId.of(this) as ChatId.ID
fun String.toChatId(): ChatId.Username = ChatId.of(this) as ChatId.Username