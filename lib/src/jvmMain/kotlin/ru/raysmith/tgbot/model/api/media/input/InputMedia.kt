package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputMediaSerializer

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * - [InputMediaAnimation]
 * - [InputMediaAudio]
 * - [InputMediaDocument]
 * - [InputMediaLivePhoto]
 * - [InputMediaPhoto]
 * - [InputMediaVideo]
 * */
@Serializable(InputMediaSerializer::class)
sealed interface InputMedia {

    /** Type of the media */
    val type: String
}
