package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal fun KSerializer<*>.fieldNotFound(filed: String): Nothing =
    error("The ${descriptor.serialName} json object must contains '$filed'")

internal fun KSerializer<*>.getPrimitive(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonPrimitive?.content ?: fieldNotFound(field)

internal fun KSerializer<*>.getJsonObject(element: JsonObject, field: String) =
    element.jsonObject[field]?.jsonObject ?: fieldNotFound(field)