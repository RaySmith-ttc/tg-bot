package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.menubutton.MenuButton
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonWebApp

object MenuButtonSerializer : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MenuButton> {
        val typeObject = element.jsonObject["type"] ?: error("The MenuButton json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The MenuButton json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The MenuButton json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "default" -> MenuButtonDefault.serializer()
            "commands" -> MenuButtonCommands.serializer()
            "web_app" -> MenuButtonWebApp.serializer()
            else -> error("Unknown MenuButton type: '$type'")
        }
    }
}

