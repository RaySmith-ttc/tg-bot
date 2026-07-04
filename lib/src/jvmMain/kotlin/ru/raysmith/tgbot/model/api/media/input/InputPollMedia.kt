package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputPollMediaSerializer

/**
 * This object represents the content of a poll description or a quiz explanation to be sent. It should be one of
 *
 * - [InputMediaAnimation]
 * - [InputMediaAudio]
 * - [InputMediaDocument]
 * - [InputMediaLivePhoto]
 * - [InputMediaLocation]
 * - [InputMediaPhoto]
 * - [InputMediaVenue]
 * - [InputMediaVideo]
 * */
@Serializable(InputPollMediaSerializer::class)
sealed interface InputPollMedia {

    /** Type of the poll media */
    val type: String
}
