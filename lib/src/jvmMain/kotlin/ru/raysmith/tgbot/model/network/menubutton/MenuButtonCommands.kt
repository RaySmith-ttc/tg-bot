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
@Serializable(with = MenuButtonCommandsSerializer::class)
object MenuButtonCommands : MenuButton() {

    /** Type of the button, must be *commands* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "commands"
}

internal object MenuButtonCommandsSerializer : KSerializer<MenuButtonCommands> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MenuButtonCommands") {
        element<String>("type")
    }
    override fun deserialize(decoder: Decoder) = MenuButtonCommands
    override fun serialize(encoder: Encoder, value: MenuButtonCommands) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
        }
    }
}