package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.chat.background.fill.BackgroundFill
import ru.raysmith.tgbot.model.network.chat.background.fill.BackgroundFillFreeformGradient
import ru.raysmith.tgbot.model.network.chat.background.fill.BackgroundFillGradient
import ru.raysmith.tgbot.model.network.chat.background.fill.BackgroundFillSolid

object BackgroundFillSerializer : JsonContentPolymorphicSerializer<BackgroundFill>(BackgroundFill::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BackgroundFill> {
        val typeObject = element.jsonObject["type"] ?: error("The BackgroundFill json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The BackgroundFill json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The BackgroundFill json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "solid" -> BackgroundFillSolid.serializer()
            "gradient" -> BackgroundFillGradient.serializer()
            "freeform_gradient" -> BackgroundFillFreeformGradient.serializer()
            else -> error("Unknown BackgroundFill type: '$type'")
        }
    }
}