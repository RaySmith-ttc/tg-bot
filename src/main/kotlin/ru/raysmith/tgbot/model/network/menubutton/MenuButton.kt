package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * - [MenuButtonCommands]
 * - [MenuButtonWebApp]
 * - [MenuButtonDefault]
 *
 * If a menu button other than MenuButtonDefault is set for a private chat, then it is applied in the chat.
 * Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 * */
@Polymorphic
@Serializable(with = MenuButtonSerializer::class)
sealed class MenuButton {
    abstract val type: String
}

