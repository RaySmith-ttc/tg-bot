package ru.raysmith.tgbot.model.api.message.rich.block

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.PhotoSize
import ru.raysmith.tgbot.model.serializer.RichBlockPhotoSerializer

/** A block with a photo, corresponding to the HTML tag <img>. */
@Serializable
data class RichBlockPhoto(

    /** Available sizes of the photo */
    @Serializable(RichBlockPhotoSerializer::class)
    @SerialName("photo") val photo: List<PhotoSize>,

    /** *True*, if the media preview is covered by a spoiler animation */
    @SerialName("has_spoiler") val hasSpoiler: Boolean? = null,

    /** Caption of the block */
    @SerialName("caption") val caption: RichBlockCaption? = null,
) : RichBlock {

    /** Type of the block, always “photo” */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "photo"
}
