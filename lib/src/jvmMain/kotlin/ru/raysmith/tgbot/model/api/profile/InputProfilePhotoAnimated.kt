package ru.raysmith.tgbot.model.api.profile

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.media.input.NotReusableInputFile

/** An animated profile photo in the MPEG4 format. */
@Serializable
data class InputProfilePhotoAnimated(

    /** The animated profile photo */
    @SerialName("animation") val animation: NotReusableInputFile,

    /** Timestamp in seconds of the frame that will be used as the static profile photo. Defaults to 0.0. */
    @SerialName("main_frame_timestamp") val mainFrameTimestamp: Float? = null,
) : InputProfilePhoto {

    /** Type of the profile photo, must be *animated* */
    @EncodeDefault
    @SerialName("type") override val type: String = "animated"
}
