package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.story.*

internal object StoryAreaTypeSerializer : ru.raysmith.tgbot.model.serializer.BaseJsonContentPolymorphicSerializer<StoryAreaType>(StoryAreaType::class) {
    override fun selectDeserializer(type: String) = when (type) {
        "location" -> StoryAreaTypeLocation.serializer()
        "suggested_reaction" -> StoryAreaTypeSuggestedReaction.serializer()
        "link" -> StoryAreaTypeLink.serializer()
        "weather" -> StoryAreaTypeWeather.serializer()
        "unique_gift" -> StoryAreaTypeUniqueGift.serializer()
        else -> null
    }

}
