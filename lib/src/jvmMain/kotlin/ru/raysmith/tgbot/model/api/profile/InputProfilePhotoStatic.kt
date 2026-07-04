package ru.raysmith.tgbot.model.api.profile

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile

/** A static profile photo in the .JPG format. */
@Serializable
data class InputProfilePhotoStatic(

    /** The static profile photo */
    @SerialName("photo") val photo: NotReusableInputFile,
) : InputProfilePhoto {

    /** Type of the profile photo, must be *static* */
    @EncodeDefault
    @SerialName("type")
    override val type: String = "static"
}
