package ru.raysmith.tgbot.model.api.media.paid

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.PhotoSize

/** The paid media is a photo. */
@Serializable
data class PaidMediaPhoto(

    /** The photo */
    val photo: List<PhotoSize>
) : PaidMedia {

    /** Type of the paid media, always “photo” */
    @EncodeDefault
    @SerialName("type") override val type: String = "photo"
}
