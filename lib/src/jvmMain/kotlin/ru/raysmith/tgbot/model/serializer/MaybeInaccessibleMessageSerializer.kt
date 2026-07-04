package ru.raysmith.tgbot.model.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.model.api.message.InaccessibleMessage
import ru.raysmith.tgbot.model.api.message.MaybeInaccessibleMessage
import ru.raysmith.tgbot.model.api.message.Message

internal object MaybeInaccessibleMessageSerializer :
    JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MaybeInaccessibleMessage> {
        return if (element.jsonObject["date"]?.jsonPrimitive?.int != 0) {
            Message.serializer()
        } else {
            InaccessibleMessage.serializer()
        }
    }
}
