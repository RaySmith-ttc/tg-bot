package ru.raysmith.tgbot.network.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import ru.raysmith.tgbot.model.network.message.InaccessibleMessage
import ru.raysmith.tgbot.model.network.message.MaybeInaccessibleMessage
import ru.raysmith.tgbot.model.network.message.Message

object MaybeInaccessibleMessageSerializer : JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MaybeInaccessibleMessage> {
        return if (element.jsonObject.size > 3) {
            Message.serializer()
        } else {
            InaccessibleMessage.serializer()
        }
    }
}