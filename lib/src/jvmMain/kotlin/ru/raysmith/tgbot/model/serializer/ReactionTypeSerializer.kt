package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.message.reaction.ReactionType
import ru.raysmith.tgbot.model.api.message.reaction.ReactionTypeCustomEmoji
import ru.raysmith.tgbot.model.api.message.reaction.ReactionTypeEmoji
import ru.raysmith.tgbot.model.api.message.reaction.ReactionTypePaid

internal object ReactionTypeSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "emoji" -> ReactionTypeEmoji.serializer()
        "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
        "paid" -> ReactionTypePaid.serializer()
        else -> null
    }
}
