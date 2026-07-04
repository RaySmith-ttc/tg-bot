package ru.raysmith.tgbot.model.api.menubutton

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents a menu button, which opens the bot's list of commands. */
@Serializable
data object MenuButtonCommands : MenuButton {

    /** Type of the button, must be *commands* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "commands"
}
