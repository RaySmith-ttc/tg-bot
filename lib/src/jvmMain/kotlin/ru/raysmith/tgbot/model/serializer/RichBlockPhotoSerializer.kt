package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.PhotoSize

/** Deserializes the `photo` field of [ru.raysmith.tgbot.model.api.message.rich.block.RichBlockPhoto] which can be
 * either an array of [PhotoSize] or a single [PhotoSize] object. */
internal object RichBlockPhotoSerializer : ArrayOrSingleSerializer<PhotoSize>(PhotoSize.serializer())
