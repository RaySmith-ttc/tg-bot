package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.network.message.reaction.ReactionType
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeCustomEmoji
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypeEmoji
import ru.raysmith.tgbot.model.network.message.reaction.ReactionTypePaid

object ReactionTypeSerializer : JsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReactionType> {
        val typeObject = element.jsonObject["type"] ?: error("The ReactionType json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The ReactionType json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The ReactionType json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "emoji" -> ReactionTypeEmoji.serializer()
            "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
            "paid" -> ReactionTypePaidSerializer
            else -> error("Unknown ReactionType type: '$type'")
        }
    }
}

object ReactionTypePaidSerializer : KSerializer<ReactionTypePaid> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ReactionTypePaid") {
        element<String>("type")
    }

    override fun deserialize(decoder: Decoder): ReactionTypePaid {
        check(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        check(element is JsonObject)

        check(element.jsonObject["type"]?.jsonPrimitive?.content == ReactionTypePaid.type) {
            "ReactionTypePaid object should have type '${ReactionTypePaid.type}' to deserialize"
        }

        return ReactionTypePaid
    }

    override fun serialize(encoder: Encoder, value: ReactionTypePaid) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, ReactionTypePaid.type)
        }
    }
}