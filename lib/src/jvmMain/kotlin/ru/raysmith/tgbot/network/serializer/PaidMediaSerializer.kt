package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*
import ru.raysmith.tgbot.model.network.media.paid.PaidMedia
import ru.raysmith.tgbot.model.network.media.paid.PaidMediaPhoto
import ru.raysmith.tgbot.model.network.media.paid.PaidMediaPreview
import ru.raysmith.tgbot.model.network.media.paid.PaidMediaVideo

object PaidMediaSerializer : JsonContentPolymorphicSerializer<PaidMedia>(PaidMedia::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<PaidMedia> {
        val typeObject = element.jsonObject["type"] ?: error("The PaidMedia json object must contains 'type' to deserialize")
        require(typeObject is JsonPrimitive) { "The PaidMedia json field 'type' must be primitive to deserialize" }
        require(typeObject.isString) { "The PaidMedia json field 'type' must be string to deserialize" }

        return when(val type = typeObject.jsonPrimitive.content) {
            "preview" -> PaidMediaPreview.serializer()
            "photo" -> PaidMediaPhoto.serializer()
            "video" -> PaidMediaVideo.serializer()
            else -> error("Unknown PaidMedia type: '$type'")
        }
    }
}