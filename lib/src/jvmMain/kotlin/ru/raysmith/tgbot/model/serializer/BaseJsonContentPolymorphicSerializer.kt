package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.exceptions.NewApiException
import kotlin.reflect.KClass

internal abstract class BaseJsonContentPolymorphicSerializer<T : Any>(
    kClass: KClass<T>, val field: String = "type"
) : JsonContentPolymorphicSerializer<T>(kClass) {
    private val className = kClass.simpleName

    abstract fun selectDeserializer(type: String): KSerializer<out T>?

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<T> {
        val typeObject = element.jsonObject[field]
            ?: error("The $className json object must contains '$field' to deserialize")
        require(typeObject is JsonPrimitive) { "The $className json field '$field' must be primitive to deserialize" }
        require(typeObject.isString) { "The $className json field '$field' must be string to deserialize" }

        val type = typeObject.jsonPrimitive.content
        return selectDeserializer(type) ?: throw NewApiException("Unknown $className $field: '$type'")
    }
}
