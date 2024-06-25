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

/** Describes that no specific value for the menu button was set. */
@Serializable(with = MenuButtonDefaultSerializer::class)
object MenuButtonDefault : MenuButton() {

    /** Type of the button, must be *default* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "default"
}

internal object MenuButtonDefaultSerializer : KSerializer<MenuButtonDefault> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MenuButtonDefault") {
        element<String>("type")
    }
    override fun deserialize(decoder: Decoder) = MenuButtonDefault
    override fun serialize(encoder: Encoder, value: MenuButtonDefault) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
        }
    }
}