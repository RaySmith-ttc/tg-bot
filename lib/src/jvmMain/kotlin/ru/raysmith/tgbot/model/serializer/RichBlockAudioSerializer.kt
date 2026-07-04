package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.Audio
import ru.raysmith.tgbot.model.api.message.rich.block.RichBlockAudio

/**
 * Deserializes the `audio` field of [RichBlockAudio] which can be either a single [Audio] object or an array of [Audio]
 * .
 * */
internal object RichBlockAudioSerializer : ArrayOrSingleSerializer<Audio>(Audio.serializer())
