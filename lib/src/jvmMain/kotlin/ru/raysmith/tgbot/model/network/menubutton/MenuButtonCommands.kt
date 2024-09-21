package ru.raysmith.tgbot.model.network.menubutton

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

/** Represents a menu button, which opens the bot's list of commands. */
@Serializable
data object MenuButtonCommands : MenuButton() {

    /** Type of the button, must be *commands* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "commands"
}