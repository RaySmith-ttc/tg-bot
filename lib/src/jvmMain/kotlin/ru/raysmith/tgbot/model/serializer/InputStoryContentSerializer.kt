package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.story.InputStoryContent
import ru.raysmith.tgbot.model.api.story.InputStoryContentPhoto
import ru.raysmith.tgbot.model.api.story.InputStoryContentVideo

internal object InputStoryContentSerializer : BaseJsonContentPolymorphicSerializer<InputStoryContent>(InputStoryContent::class) {
    override fun selectDeserializer(type: String) = when(type) {
        "photo" -> InputStoryContentPhoto.serializer()
        "video" -> InputStoryContentVideo.serializer()
        else -> null
    }
}
