package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.Animation
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockAnimation

/**
 * Deserializes the `animation` field of [RichBlockAnimation] which can be either a single [Animation] object or an
 * array of [Animation].
 * */
internal object RichBlockAnimationSerializer : ArrayOrSingleSerializer<Animation>(Animation.serializer(),)
