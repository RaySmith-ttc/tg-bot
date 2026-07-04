package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.message.origin.*

internal object MessageOriginSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<MessageOrigin>(MessageOrigin::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "user" -> MessageOriginUser.serializer()
        "hidden_user" -> MessageOriginHiddenUser.serializer()
        "chat" -> MessageOriginChat.serializer()
        "channel" -> MessageOriginChannel.serializer()
        else -> null
    }
}
