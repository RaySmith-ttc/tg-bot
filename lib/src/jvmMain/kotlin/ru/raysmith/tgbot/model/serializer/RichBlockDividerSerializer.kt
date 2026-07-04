package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockDivider

/**
 * Custom serializer for [RichBlockDivider].
 *
 * Kotlin serialization always serializes `object`/`data object` declarations as an empty structure, ignoring any
 * properties declared in their body. Since [RichBlockDivider] must still encode its `type` discriminator, this
 * serializer implements the structure manually instead of relying on the compiler-generated one.
 * */
internal object RichBlockDividerSerializer : KSerializer<RichBlockDivider> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RichBlockDivider") {
        element<String>("type")
    }

    override fun serialize(encoder: Encoder, value: RichBlockDivider) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
        }
    }

    override fun deserialize(decoder: Decoder): RichBlockDivider {
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> decodeStringElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> continue
                }
            }
        }
        return RichBlockDivider
    }
}
