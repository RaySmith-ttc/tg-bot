package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.menubutton.MenuButton
import ru.raysmith.tgbot.model.api.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.api.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.api.menubutton.MenuButtonWebApp

internal object MenuButtonSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "default" -> MenuButtonDefault.serializer()
        "commands" -> MenuButtonCommands.serializer()
        "web_app" -> MenuButtonWebApp.serializer()
        else -> null
    }
}
