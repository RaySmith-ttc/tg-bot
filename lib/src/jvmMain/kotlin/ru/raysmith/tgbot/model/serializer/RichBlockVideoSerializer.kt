package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.Video

/** Deserializes the `video` field of [ru.raysmith.tgbot.model.api.message.rich.block.RichBlockVideo] which can be
 * either a single [Video] object or an array of [Video]. */
internal object RichBlockVideoSerializer : ArrayOrSingleSerializer<Video>(Video.serializer())
