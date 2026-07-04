package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.raysmith.tgbot.model.api.message.rich.text.RichText
import ru.raysmith.tgbot.model.api.message.rich.text.RichTextList

/** Serializes/deserializes a [RichTextList] as a raw JSON array of [RichText]. */
internal object RichTextListSerializer : KSerializer<RichTextList> {
    private val delegate = ListSerializer(RichTextSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: RichTextList) {
        encoder.encodeSerializableValue(delegate, value.items)
    }

    override fun deserialize(decoder: Decoder): RichTextList {
        return RichTextList(decoder.decodeSerializableValue(delegate))
    }
}
