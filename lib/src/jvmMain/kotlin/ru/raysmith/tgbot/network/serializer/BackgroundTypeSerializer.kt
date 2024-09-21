package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.chat.background.*

object BackgroundTypeSerializer : JsonContentPolymorphicSerializer<BackgroundType>(BackgroundType::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BackgroundType> {
        val typeObject = element.jsonObject["type"] ?: error("The BackgroundType json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The BackgroundType json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The BackgroundType json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "fill" -> BackgroundTypeFill.serializer()
            "wallpaper" -> BackgroundTypeWallpaper.serializer()
            "pattern" -> BackgroundTypePattern.serializer()
            "chat_theme" -> BackgroundTypeChatTheme.serializer()
            else -> error("Unknown BackgroundType type: '$type'")
        }
    }
}