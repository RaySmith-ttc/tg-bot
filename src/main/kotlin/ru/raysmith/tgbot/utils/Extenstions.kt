package ru.raysmith.tgbot.utils

import kotlinx.serialization.builtins.ListSerializer
import ru.raysmith.tgbot.model.network.updates.UpdateType
import ru.raysmith.tgbot.network.TelegramApi

fun List<UpdateType>.asParameter(): String {
//    return joinToString(",", "[", "]") { "\"${it.name.lowercase()}\"" }
    return TelegramApi.json.encodeToJsonElement(ListSerializer(UpdateType.serializer()), this).toString()
}

fun <T> MutableList<T>.addIFNotContains(el: T) {
    if (!this.contains(el)) add(el)
}