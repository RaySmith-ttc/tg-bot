package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputPollOptionMediaSerializer

/**
 * This object represents the content of a poll option to be sent. It should be one of
 *
 * - [InputMediaAnimation]
 * - [InputMediaLink]
 * - [InputMediaLivePhoto]
 * - [InputMediaLocation]
 * - [InputMediaPhoto]
 * - [InputMediaSticker]
 * - [InputMediaVenue]
 * - [InputMediaVideo]
 * */
@Serializable(InputPollOptionMediaSerializer::class)
sealed interface InputPollOptionMedia {

    /** Type of the poll option media */
    val type: String
}
