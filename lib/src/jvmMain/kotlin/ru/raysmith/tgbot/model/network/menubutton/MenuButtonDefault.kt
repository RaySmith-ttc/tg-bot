package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.MenuButtonDefaultSerializer

/** Describes that no specific value for the menu button was set. */
@Serializable(with = MenuButtonDefaultSerializer::class)
data object MenuButtonDefault : MenuButton() {

    /** Type of the button, must be *default* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "default"
}