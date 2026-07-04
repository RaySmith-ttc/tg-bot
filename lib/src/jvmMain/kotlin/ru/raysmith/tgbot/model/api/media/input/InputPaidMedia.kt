package ru.raysmith.tgbot.model.api.media.input

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.InputPaidMediaSerializer

/**
 * This object describes the paid media to be sent. Currently, it can be one of
 *
 * - [InputPaidMediaLivePhoto]
 * - [InputPaidMediaPhoto]
 * - [InputPaidMediaVideo]
 * */
@Serializable(InputPaidMediaSerializer::class)
sealed interface InputPaidMedia : InputWithMedia {

    /** Type of the media */
    val type: String
}
