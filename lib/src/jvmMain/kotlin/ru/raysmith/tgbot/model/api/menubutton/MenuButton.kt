package ru.raysmith.tgbot.model.api.menubutton

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.MenuButtonSerializer

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 *
 * - [MenuButtonCommands]
 * - [MenuButtonWebApp]
 * - [MenuButtonDefault]
 *
 * If a menu button other than [MenuButtonDefault] is set for a private chat, then it is applied in the chat. Otherwise
 * the default menu button is applied. By default, the menu button opens the list of bot commands.
 * */
@Serializable(MenuButtonSerializer::class)
sealed interface MenuButton {

    /** Type of the button */
    val type: String
}

