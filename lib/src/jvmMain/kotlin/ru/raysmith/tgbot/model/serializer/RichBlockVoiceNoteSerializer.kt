package ru.raysmith.tgbot.model.serializer

import ru.raysmith.tgbot.model.api.media.Voice

/** Deserializes the `voice_note` field of [ru.raysmith.tgbot.model.api.message.rich.block.RichBlockVoiceNote] which
 * can be either a single [Voice] object or an array of [Voice]. */
internal object RichBlockVoiceNoteSerializer : ArrayOrSingleSerializer<Voice>(Voice.serializer())
