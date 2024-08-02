package ru.raysmith.tgbot.model.network.media.paid

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.media.PhotoSize

/** The paid media is a photo. */
@Serializable
data class PaidMediaPhoto(

    /** The photo */
    val photo: List<PhotoSize>
) : PaidMedia() {

    /** Type of the paid media, always “photo” */
    override val type: String = "photo"
}