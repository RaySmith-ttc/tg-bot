package ru.raysmith.tgbot.model.api.media.paid

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.serializer.PaidMediaSerializer

/**
 * This object describes paid media. Currently, it can be one of
 *
 * - [PaidMediaLivePhoto]
 * - [PaidMediaPhoto]
 * - [PaidMediaPreview]
 * - [PaidMediaVideo]
 * */
@Serializable(PaidMediaSerializer::class)
sealed interface PaidMedia {

    /** Type of the paid media */
    val type: String
}
