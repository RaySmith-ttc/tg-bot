package ru.raysmith.tgbot.model.network.media.paid

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.network.serializer.PaidMediaSerializer

/**
 * This object describes paid media. Currently, it can be one of
 *
 * - [PaidMediaPreview]
 * - [PaidMediaPhoto]
 * - [PaidMediaVideo]
 * */
@Serializable(with = PaidMediaSerializer::class)
sealed class PaidMedia {

    /** Type of the paid media */
    abstract val type: String
}