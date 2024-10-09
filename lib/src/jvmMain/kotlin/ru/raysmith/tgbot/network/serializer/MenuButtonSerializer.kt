package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
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
            "default" -> MenuButtonDefaultSerializer
            "commands" -> MenuButtonCommandsSerializer
            "web_app" -> MenuButtonWebApp.serializer()
            else -> error("Unknown MenuButton type: '$type'")
        }
    }
}

object MenuButtonDefaultSerializer : KSerializer<MenuButtonDefault> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MenuButtonDefault") {
        element<String>("type")
    }
    override fun deserialize(decoder: Decoder): MenuButtonDefault {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == MenuButtonDefault.type) {
            "MenuButtonDefault object should have type '${MenuButtonDefault.type}' to deserialize"
        }

        return MenuButtonDefault
    }

    override fun serialize(encoder: Encoder, value: MenuButtonDefault) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, MenuButtonDefault.type)
        }
    }

}

object MenuButtonCommandsSerializer : KSerializer<MenuButtonCommands> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MenuButtonCommands") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): MenuButtonCommands {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == MenuButtonCommands.type) {
            "MenuButtonCommands object should have type '${MenuButtonCommands.type}' to deserialize"
        }

        return MenuButtonCommands
    }

    override fun serialize(encoder: Encoder, value: MenuButtonCommands) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, MenuButtonCommands.type)
        }
    }
}
